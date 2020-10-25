package com.example.savethefood.data.source

import androidx.lifecycle.LiveData
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.FoodSearchDomain
import com.example.savethefood.data.source.local.entity.FoodEntity
import kotlinx.coroutines.flow.Flow

interface FoodDataSource {

    @Throws(Exception::class)
    suspend fun getFoodByUpc(barcode: String): Result<FoodDomain>

    @Throws(Exception::class)
    suspend fun getFoodByQuery(barcode: String): Result<FoodSearchDomain>?

    @Throws(Exception::class)
    suspend fun getFoodById(id: Int): Result<FoodDomain>

    suspend fun insertFood(food: FoodDomain): Long

    suspend fun updateFoods(food: FoodDomain)

    suspend fun getFoods(): Flow<List<FoodEntity>?>

    suspend fun getLocalFoods(): Result<List<FoodDomain>>

    suspend fun deleteFood(food: FoodDomain?): Int
}