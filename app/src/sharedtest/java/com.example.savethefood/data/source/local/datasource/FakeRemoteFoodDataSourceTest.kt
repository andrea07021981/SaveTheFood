package com.example.savethefood.data.source.local.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.FoodSearchDomain
import com.example.savethefood.data.source.FoodDataSource
import com.example.savethefood.data.source.local.entity.FoodEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteFoodDataSourceTest(
    private val foodList: MutableList<FoodDomain> = mutableListOf()
) : FoodDataSource {

    private var _foodList = MediatorLiveData<Result<List<FoodDomain>>>()

    init {
        _foodList.value = Result.Success(foodList)
    }
    override suspend fun getFoodByUpc(barcode: String): Result<FoodDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun getFoodByQuery(barcode: String): Result<FoodSearchDomain>? {
        TODO("Not yet implemented")
        //TODO parse query an test the url offline
    }

    override suspend fun getFoodById(id: Int): Result<FoodDomain>{
        foodList.find {
            it.foodId == id
        }.also {
            return if (it != null) {
                Result.Success(it)
            } else {
                Result.Error("Not found online")
            }
        }
    }

    override suspend fun insertFood(food: FoodDomain): Long {
        foodList.add(food).also { return food.foodId.toLong() }
    }

    override suspend fun updateFoods(food: FoodDomain) {
        TODO("Not yet implemented")
    }

    override suspend fun getFoods(): Flow<List<FoodEntity>?> = flow {
        _foodList
    }

    override suspend fun getLocalFoods(): Result<List<FoodDomain>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFood(food: FoodDomain?): Int {
        TODO("Not yet implemented")
    }
}