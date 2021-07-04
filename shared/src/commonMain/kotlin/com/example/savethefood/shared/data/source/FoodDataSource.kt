package com.example.savethefood.shared.data.source

import com.example.savethefood.shared.data.ActionResult
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.data.domain.FoodSearchDomain
import com.example.savethefood.shared.data.source.local.entity.FoodEntity
import kotlinx.coroutines.flow.Flow

interface FoodDataSource {

    @Throws(Exception::class)
    suspend fun getFoodByUpc(barcode: String): ActionResult<FoodDomain>

    @Throws(Exception::class)
    suspend fun getFoodByQuery(barcode: String): ActionResult<FoodSearchDomain>?

    @Throws(Exception::class)
    suspend fun getFoodById(id: Int): ActionResult<FoodDomain>

    suspend fun insertNewFood(food: FoodDomain): Long

    fun getFoods(): Flow<List<FoodDomain>?>

    suspend fun getLocalFoods(): ActionResult<List<FoodDomain>>

    suspend fun deleteFood(food: FoodEntity): Long?
}