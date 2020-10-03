package com.example.savethefood.data.source.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.FoodSearchDomain
import com.example.savethefood.data.source.FoodDataSource
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.source.local.datasource.FoodLocalDataSource
import com.example.savethefood.data.source.remote.datasource.FoodRemoteDataSource
import com.example.savethefood.data.source.remote.service.ApiClient
import com.example.savethefood.util.wrapEspressoIdlingResource
import kotlinx.coroutines.*

class FoodDataRepository(
    private val foodLocalDataSource: FoodDataSource,
    private val foodRemoteDataSource: FoodDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FoodRepository {

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
    }
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
                return@coroutineScope Result.Error("Not retrieved")
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
    override suspend fun saveNewFood(food: FoodDomain) = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            foodLocalDataSource.insertFood(food)
        }
    }

    override suspend fun getFoods(): LiveData<Result<List<FoodDomain>>> = withContext(Dispatchers.IO) {
        wrapEspressoIdlingResource {
            foodLocalDataSource.getFoods()
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