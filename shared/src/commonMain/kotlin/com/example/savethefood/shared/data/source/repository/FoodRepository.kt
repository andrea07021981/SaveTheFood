package com.example.savethefood.shared.data.source.repository

import com.example.savethefood.shared.data.ActionResult
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.data.domain.FoodItem
import com.example.savethefood.shared.data.domain.FoodSearchDomain
import com.example.savethefood.shared.utils.FoodImage
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KProperty1

interface FoodRepository {
    @Throws(Exception::class)
    suspend fun getApiFoodUpc(barcode: String): ActionResult<FoodDomain>

    suspend fun getApiFoodQuery(query: String): ActionResult<FoodSearchDomain>?

    suspend fun getApiFoodById(id: Int): ActionResult<FoodDomain>

    suspend fun refreshData()

    suspend fun saveNewFood(food: FoodDomain): ActionResult<FoodDomain>

    fun getFoods(): Flow<ActionResult<List<FoodDomain>>>

    suspend fun getLocalFoods(): ActionResult<List<FoodDomain>>

    suspend fun deleteFood(food: FoodDomain): Long?

    fun getFoodImages(
        orderField: KProperty1<FoodImage, String> = FoodImage::name
    ): LinkedHashSet<FoodItem>
}