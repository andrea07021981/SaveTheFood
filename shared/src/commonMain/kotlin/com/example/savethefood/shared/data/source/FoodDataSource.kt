package com.example.savethefood.shared.data.source

import com.example.savethefood.shared.data.Result
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.data.domain.FoodSearchDomain
import com.example.savethefood.shared.data.source.local.entity.FoodEntity
import kotlinx.coroutines.flow.Flow

interface FoodDataSource {

    val tag: String
        get() = FoodDataSource::class.simpleName!!

    @Throws(Exception::class)
    suspend fun getFoodByUpc(barcode: String): Result<FoodDomain>

    @Throws(Exception::class)
    suspend fun getFoodByQuery(barcode: String): Result<FoodSearchDomain>?

    @Throws(Exception::class)
    suspend fun getFoodById(id: Int): Result<FoodDomain>

    suspend fun updateFoods(food: FoodDomain)

    suspend fun insertNewFood(food: FoodDomain): Long

    @Throws(Exception::class)
    fun getFoods(): Flow<List<FoodDomain>?>

    suspend fun getLocalFoods(): Result<List<FoodDomain>>

    suspend fun deleteFood(food: FoodEntity): Long?
}