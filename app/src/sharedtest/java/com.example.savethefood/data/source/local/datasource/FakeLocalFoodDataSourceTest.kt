package com.example.savethefood.data.source.local.datasource

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.FoodSearchDomain
import com.example.savethefood.data.source.FoodDataSource
import com.example.savethefood.data.source.local.entity.FoodEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalFoodDataSourceTest(
    private val foodList: MutableList<FoodDomain> = mutableListOf()
) : FoodDataSource {

    override suspend fun getFoodByUpc(barcode: String): Result<FoodDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun getFoodByQuery(barcode: String): Result<FoodSearchDomain>? {
        TODO("Not yet implemented")
    }

    override suspend fun getFoodById(id: Int): Result<FoodDomain>{
        foodList.find {
            it.id == id
        }.also {
            return if (it != null) {
                Result.Success(it)
            } else {
                Result.Error("Not found locally")
            }
        }
    }

    override suspend fun insertFood(food: FoodDomain): Long {
        foodList.add(food).also { return food.id.toLong() }
    }

    override suspend fun updateFoods(food: FoodDomain) {
        TODO("Not yet implemented")
    }

    override fun getFoods(): Flow<List<FoodEntity>> = flow {
        foodList
    }

    override suspend fun getLocalFoods(): Result<List<FoodDomain>> {
        return Result.Success(foodList)
    }

    override suspend fun deleteFood(food: FoodEntity): Int {
        foodList.remove(food).also {
            return food!!.id
        }
    }
}