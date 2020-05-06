package com.example.savethefood.data.source.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.FoodDataSource
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.source.local.datasource.FoodLocalDataSource
import com.example.savethefood.data.source.remote.datasource.FoodRemoteDataSource
import com.example.savethefood.data.source.remote.service.ApiClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

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
    /**may throw Exception, with coroutineScope is possible Exception will cancell only the coroutines created in
    *This scope, without touching the outer scope. We could avoid it and use the supervisor job in VM, but this way is more efficient
    */
    @Throws(Exception::class)
    override suspend fun getApiFoodUpc(barcode: String): Result<FoodDomain> = coroutineScope{
        foodRemoteDataSource.getFoodByUpc(barcode)
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
        foodLocalDataSource.insertFood(food)
    }

    override suspend fun getFoods(): LiveData<Result<List<FoodDomain>>> = withContext(Dispatchers.IO) {
        foodLocalDataSource.getFoods()
    }

    override suspend fun deleteFood(food: FoodDomain?)  = withContext(Dispatchers.IO) {
        foodLocalDataSource.deleteFood(food)
    }
}