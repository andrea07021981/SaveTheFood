package com.example.savethefood.data.source.repository

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.FoodSearchDomain
import com.example.savethefood.data.domain.RecipeIngredients
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    @Throws(Exception::class)
    suspend fun getApiFoodUpc(barcode: String): Result<FoodDomain>

    suspend fun getApiFoodQuery(query: String): Result<FoodSearchDomain>?

    suspend fun getApiFoodById(id: Int): Result<FoodDomain>

    suspend fun refreshData()

    suspend fun saveNewFood(food: FoodDomain): Result<FoodDomain>

    fun getFoods(): Flow<Result<List<FoodDomain>>>

    suspend fun getLocalFoods(): Result<List<FoodDomain>>

    suspend fun deleteFood(food: FoodDomain?): Int
}