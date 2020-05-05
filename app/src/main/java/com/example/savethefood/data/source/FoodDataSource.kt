package com.example.savethefood.data.source

import androidx.lifecycle.LiveData
import com.example.savethefood.data.domain.FoodDomain

interface FoodDataSource {

    @Throws(Exception::class)
    suspend fun getFoodByUpc(barcode: String): FoodDomain?

    suspend fun saveNewFood(food: FoodDomain)

    suspend fun getFoods(): LiveData<List<FoodDomain>>

    suspend fun deleteFood(food: FoodDomain?)
}