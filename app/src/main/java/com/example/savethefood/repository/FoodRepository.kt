package com.example.savethefood.repository

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodRepository(private val database: SaveTheFoodDatabase) {

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