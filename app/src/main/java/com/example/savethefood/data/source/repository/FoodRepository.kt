package com.example.savethefood.data.source.repository

import androidx.lifecycle.LiveData
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain

interface FoodRepository {
    @Throws(Exception::class)
    suspend fun getApiFoodUpc(barcode: String): Result<FoodDomain>

    suspend fun saveNewFood(food: FoodDomain): Long

    suspend fun getFoods(): LiveData<List<FoodDomain>>

    suspend fun deleteFood(food: FoodDomain?)
}