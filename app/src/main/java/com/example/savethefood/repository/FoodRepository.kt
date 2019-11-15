package com.example.savethefood.repository

import android.util.Log
import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.local.domain.Food
import com.example.savethefood.local.domain.asDatabaseModel
import com.example.savethefood.local.entity.FoodEntity
import com.example.savethefood.local.entity.asDomainModel
import com.example.savethefood.network.datatransferobject.asDatabaseModel
import com.example.savethefood.network.service.FoodApi
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import java.lang.Exception

class FoodRepository(private val database: SaveTheFoodDatabase) {

    suspend fun getApiFoodUpc() {
        //Withcontext is a function that allows to easily change the context that will be used to run a part of the code inside a coroutine. This is a suspending function, so it means that it’ll suspend the coroutine until the code inside is executed, no matter the dispatcher that it’s used.
        //With that, we can make our suspending functions use a different thread:
        withContext(Dispatchers.IO) {
            try {
                val foodData = FoodApi.retrofitService.getFoodByUpc("041631000564").await() ?: return@withContext
                Log.d("JSON RESULT", foodData.id.toString())
                //Todo add to db or return the value and save right after
                database.foodDatabaseDao.insert(foodData.asDatabaseModel())
            } catch (error: JsonDataException) {
                Log.d("Error retrofit", error.message)
            }
        }
    }

    suspend fun saveNewFood(food: Food) {
        withContext(Dispatchers.IO) {
            val newId = database.foodDatabaseDao.insert(food.asDatabaseModel())
        }
    }

    suspend fun getFoods(): LiveData<List<Food>> {
        return withContext(Dispatchers.IO) {
            Transformations.map(database.foodDatabaseDao.getFoods()) {
                it.asDomainModel()
            }
        }
    }
}