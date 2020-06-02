package com.example.savethefood.data.source

import androidx.lifecycle.LiveData
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.FoodSearchDomain

interface FoodDataSource {

    @Throws(Exception::class)
    suspend fun getFoodByUpc(barcode: String): Result<FoodDomain>

    @Throws(Exception::class)
    suspend fun getFoodByQuery(barcode: String): Result<FoodSearchDomain>

    suspend fun insertFood(food: FoodDomain): Long

    suspend fun getFoods(): LiveData<Result<List<FoodDomain>>>

    suspend fun deleteFood(food: FoodDomain?)
}