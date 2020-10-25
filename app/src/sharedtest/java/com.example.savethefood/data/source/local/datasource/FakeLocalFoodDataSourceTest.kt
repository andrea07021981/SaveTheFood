package com.example.savethefood.data.source.local.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
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
            it.foodId == id
        }.also {
            return if (it != null) {
                Result.Success(it)
            } else {
                Result.Error("Not found locally")
            }
        }
    }

    override suspend fun insertFood(food: FoodDomain): Long {
        foodList.add(food).also { return food.foodId.toLong() }
    }

    override suspend fun updateFoods(food: FoodDomain) {
        TODO("Not yet implemented")
    }

    override suspend fun getFoods(): Flow<List<FoodEntity>> = flow {
        foodList
    }

    override suspend fun getLocalFoods(): Result<List<FoodDomain>> {
        return Result.Success(foodList)
    }

    override suspend fun deleteFood(food: FoodDomain?): Int {
        foodList.remove(food).also {
            return food!!.foodId
        }
    }
}