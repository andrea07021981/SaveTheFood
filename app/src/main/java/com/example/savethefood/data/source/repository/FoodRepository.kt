package com.example.savethefood.data.source.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.asDatabaseModel
import com.example.savethefood.data.source.local.entity.asDomainModel
import com.example.savethefood.data.source.remote.datatransferobject.asDomainModel
import com.example.savethefood.data.source.remote.service.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.lang.Exception

class FoodRepository(
    private val database: SaveTheFoodDatabase
) {

    /**may throw Exception, with coroutineScope is possible Exception will cancell only the coroutines created in
    *This scope, without touching the outer scope. We could avoid it and use the supervisor job in VM, but this way is more efficient
    */
    @Throws(Exception::class)
    suspend fun getApiFoodUpc(barcode: String): FoodDomain? = coroutineScope{
        try {
            val foodData = ApiClient.retrofitService.getFoodByUpc(barcode).await()
            Log.d("JSON RESULT", foodData.id.toString())
            return@coroutineScope foodData.asDomainModel()
        } catch (e: Exception) {
            throw Exception(e)
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
    suspend fun saveNewFood(food: FoodDomain) {
        withContext(Dispatchers.IO) {
            val newId = database.foodDatabaseDao.insert(food.asDatabaseModel())
        }
    }

    suspend fun getFoods(): LiveData<List<FoodDomain>> {
        return withContext(Dispatchers.IO) {
            Transformations.map(database.foodDatabaseDao.getFoods()) {
                it.asDomainModel()
            }
        }
    }

    suspend fun deleteFood(food: FoodDomain?) {
        withContext(Dispatchers.IO) {
                database.foodDatabaseDao.deleteFood(food = food!!.asDatabaseModel())
        }
    }
}