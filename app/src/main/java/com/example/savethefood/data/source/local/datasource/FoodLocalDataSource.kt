package com.example.savethefood.data.source.local.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.asDatabaseModel
import com.example.savethefood.data.source.FoodDataSource
import com.example.savethefood.data.source.local.dao.FoodDatabaseDao
import com.example.savethefood.data.source.local.entity.asDomainModel
import com.example.savethefood.data.source.remote.datatransferobject.asDomainModel
import com.example.savethefood.data.source.remote.service.ApiClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.lang.Exception

class FoodLocalDataSource internal constructor(
    private val foodDatabaseDao: FoodDatabaseDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FoodDataSource {

    override suspend fun getFoodByUpc(barcode: String): FoodDomain? {
        TODO("No OP")
    }

    /**
    This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     * ----------------------------------------------------------------------------
     * Withcontext is a function that allows to easily change the context that will be used to run a part of the code inside a coroutine.
     * This is a suspending function, so it means that it’ll suspend the coroutine until the code inside is executed, no matter the dispatcher that it’s used.
     * With that, we can make our suspending functions use a different thread:
     */
    override suspend fun saveNewFood(food: FoodDomain) {
        withContext(ioDispatcher) {
            val newId = foodDatabaseDao.insert(food.asDatabaseModel())
        }
    }

    override suspend fun getFoods(): LiveData<List<FoodDomain>> {
        return withContext(ioDispatcher) {
            Transformations.map(foodDatabaseDao.getFoods()) {
                it.asDomainModel()
            }
        }
    }

    override suspend fun deleteFood(food: FoodDomain?) {
        withContext(ioDispatcher) {
            foodDatabaseDao.deleteFood(food = food!!.asDatabaseModel())
        }
    }
}