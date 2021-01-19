package com.example.savethefood.data.source.repository

import androidx.lifecycle.LiveData
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.FoodSearchDomain
import com.example.savethefood.data.source.FoodDataSource
import com.example.savethefood.data.source.local.entity.asDomainModel
import com.example.savethefood.di.BaseModule
import com.example.savethefood.util.wrapEspressoIdlingResource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject

//TODO Repository should receive base data (Network domain ex), and convert THEN EMIT

/**
 *
 *
 * TODO DATASOURCE RETURN BASE DATA, REPO EMIT LOADING, RESULT, ETC, VIEWMODEL EXPOSE LIVEDATA
 *
 * TODO use emit here
 *
 *
 *
 */
@ExperimentalCoroutinesApi
class FoodDataRepository @Inject constructor(
    private val foodLocalDataSource: FoodDataSource,
    private val foodRemoteDataSource: FoodDataSource
) : FoodRepository {
/*
    companion object {
        @Volatile
        private var INSTANCE: FoodDataRepository? = null

        fun getRepository(app: Application): FoodDataRepository {
            return INSTANCE ?: synchronized(this) {
                val database = SaveTheFoodDatabase.getInstance(app)
                return FoodDataRepository(
                    FoodLocalDataSource(database.foodDatabaseDao),
                    FoodRemoteDataSource(ApiClient.retrofitService)
                ).also {
                    INSTANCE = it
                }
            }
        }
    }*/

    /**may throw Exception, with coroutineScope is possible Exception will cancel only the coroutines created in
    *This scope, without touching the outer scope. We could avoid it and use the supervisor job in VM, but this way is more efficient
    */
    @Throws(Exception::class)
    override suspend fun getApiFoodUpc(barcode: String): Result<FoodDomain> = coroutineScope{
        wrapEspressoIdlingResource {
            val foodRetrieved = foodRemoteDataSource.getFoodByUpc(barcode)
            if (foodRetrieved is Result.Success) {
                val saveNewFood = foodLocalDataSource.insertFood(foodRetrieved.data)
                if (saveNewFood == 0L) {
                    return@coroutineScope Result.Error("Not saved")
                } else {
                    return@coroutineScope Result.Success(foodRetrieved.data)
                }
            } else {
                return@coroutineScope foodRetrieved
            }
        }
    }

    @Throws(Exception::class)
    override suspend fun getApiFoodQuery(query: String): Result<FoodSearchDomain>? = coroutineScope{
        wrapEspressoIdlingResource {
            foodRemoteDataSource.getFoodByQuery(query)
        }
    }

    override suspend fun getApiFoodById(id: Int): Result<FoodDomain> = coroutineScope{
        wrapEspressoIdlingResource {
            val foodByIdResult = foodRemoteDataSource.getFoodById(id)
            if (foodByIdResult is Result.Success) {
                foodLocalDataSource.insertFood(foodByIdResult.data)
                return@coroutineScope foodByIdResult
            } else {
                throw Exception(foodByIdResult.toString())
            }
        }
    }

    override suspend fun refreshData() = coroutineScope{
        wrapEspressoIdlingResource {
            val localFoods = foodLocalDataSource.getLocalFoods()
            if (localFoods is Result.Success) {
                for (food in localFoods.data) {
                    val foodById = foodRemoteDataSource.getFoodById(food.foodId)
                    if (foodById is Result.Success) {
                        //TODO change, create a fooddomain container like devbytes and pass one shot, are varargs
                        foodLocalDataSource.updateFoods(foodById.data)
                    }
                }

            } else{
                throw Exception(localFoods.toString())
            }
        }
    }


    /**
      This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     * ----------------------------------------------------------------------------
     * Withcontext is a function that allows to easily change the context that will be used to run a part of the code inside a coroutine. This is a suspending function, so it means that it’ll suspend the coroutine until the code inside is executed, no matter the dispatcher that it’s used.
     * With that, we can make our suspending functions use a different thread:
     */
    override suspend fun saveNewFood(food: FoodDomain) = withContext(Dispatchers.IO) {
        wrapEspressoIdlingResource {
            foodLocalDataSource.insertFood(food)
        }
    }

    /*remove flow from database, here do like fish override suspend fun getFoods(): Flow<Result<List<FoodDomain>>> = flow {
    try {
        emit(kotlin.Result.Loading)
        val authResultAwait = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        if (authResultAwait.user != null) {
            emit(kotlin.Result.Success(authResultAwait.user!!))
        } else {
            emit(kotlin.Result.Error("Login Failed"))
        }
    } catch (e: Exception) {
        //Print the message but send a login failed info
        e.printStackTrace()
        emit(kotlin.Result.Error("Login Error"))
    }*/
    // In coroutines, a flow is a type that can emit multiple values sequentially,
    // as opposed to suspend functions that return only a single value. For example, you can use a flow to receive live updates from a database
    // TODO remove all suspend where we use flow, no needed
    override suspend fun getFoods(): Flow<Result<List<FoodDomain>>> {
        wrapEspressoIdlingResource {
            delay(1000) // TEST long time
            //TODO DO LIKE RECIPE. Moreover add a retry in case of error, with a custom number of attempts (maybe retryWhen() or retry()?)
            return foodLocalDataSource.getFoods()
                .onStart {
                    Result.Loading
                }
                .onEach {
                    check(it != null)
                }
                .catch {
                    Result.ExError(Exception(""))
                }
                .map {
                    if (it!!.isNotEmpty()) {
                        Result.Success(it.asDomainModel())
                    } else {
                        Result.Error("No data")
                    }
                }.retryWhen {cause, attempt ->
                    if (cause is IOException && attempt < 5) {    // retry on IOException
                        delay(1000)                     // delay for one second before retry
                        true
                    } else {                                      // do not retry otherwise
                        false
                    }
                }
                // FLOWON is the correct way to change the context. The collection remains in main thread, but this flow goes in IO concurrently
                .flowOn(Dispatchers.IO)
                // We can tell flow to make the buffer "conflated". It removes the buffer from flowOn
                // and only shares the last value, as our UI discards any intermediate values in the
                // buffer.
                .conflate()
        }
    }

    override suspend fun getLocalFoods(): Result<List<FoodDomain>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFood(food: FoodDomain?): Int  = withContext(Dispatchers.IO) {
        wrapEspressoIdlingResource {
            foodLocalDataSource.deleteFood(food)
        }
    }
}