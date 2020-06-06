package com.example.savethefood.data.source.local.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.FoodSearchDomain
import com.example.savethefood.data.source.FoodDataSource

class FakeFoodDataSourceTest(
    val foodList: MutableList<FoodDomain> = mutableListOf()
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
    }

    override suspend fun getFoodById(id: Int): Result<FoodDomain>{
        TODO("Not yet implemented")
    }

    override suspend fun insertFood(food: FoodDomain): Long {
        TODO("Not yet implemented")
    }

    override suspend fun getFoods(): LiveData<Result<List<FoodDomain>>> {
        return _foodList
    }

    override suspend fun deleteFood(food: FoodDomain?): Int {
        TODO("Not yet implemented")
    }
}