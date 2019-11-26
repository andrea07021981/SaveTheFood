package com.example.savethefood.repository

import android.util.Log
import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.local.domain.FoodDomain
import com.example.savethefood.local.domain.asDatabaseModel
import com.example.savethefood.local.entity.FoodEntity
import com.example.savethefood.local.entity.asDomainModel
import com.example.savethefood.network.datatransferobject.asDatabaseModel
import com.example.savethefood.network.datatransferobject.asDomainModel
import com.example.savethefood.network.service.FoodApi
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.json.JSONException
import java.lang.Exception
import java.security.spec.ECField

class FoodRepository(private val database: SaveTheFoodDatabase) {

    // may throw Exception, with coroutineScope is possible Exception will cancell only the coroutines created in
    //This scope, without touching the outer scope
    @Throws(Exception::class)
    suspend fun getApiFoodUpc(barcode: String): FoodDomain? = coroutineScope{
        try {
            val foodData = FoodApi.retrofitService.getFoodByUpc(barcode).await()
            Log.d("JSON RESULT", foodData.id.toString())
            foodData.asDomainModel()
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    //Withcontext is a function that allows to easily change the context that will be used to run a part of the code inside a coroutine. This is a suspending function, so it means that it’ll suspend the coroutine until the code inside is executed, no matter the dispatcher that it’s used.
    //With that, we can make our suspending functions use a different thread:
    /*
      This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
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
}