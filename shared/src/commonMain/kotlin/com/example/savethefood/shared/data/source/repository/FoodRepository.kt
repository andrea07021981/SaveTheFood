package com.example.savethefood.shared.data.source.repository

import com.example.savethefood.shared.data.Result
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.data.domain.FoodItem
import com.example.savethefood.shared.data.domain.FoodSearchDomain
import com.example.savethefood.shared.utils.FoodImage
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KProperty1

interface FoodRepository {
    @Throws(Exception::class)
    suspend fun getApiFoodUpc(barcode: String): Result<FoodDomain>

    suspend fun getApiFoodQuery(query: String): Result<FoodSearchDomain>?

    suspend fun getApiFoodById(id: Int): Result<FoodDomain>

    suspend fun refreshData()

    suspend fun saveNewFood(food: FoodDomain): Result<FoodDomain>

    fun getFoods(): Flow<Result<List<FoodDomain>>>

    suspend fun getLocalFoods(): Result<List<FoodDomain>>

    suspend fun deleteFood(food: FoodDomain): Long?

    fun getFoodImages(
        orderField: KProperty1<FoodImage, String> = FoodImage::name
    ): LinkedHashSet<FoodItem>
}