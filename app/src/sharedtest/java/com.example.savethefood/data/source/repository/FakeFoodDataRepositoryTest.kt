package com.example.savethefood.data.source.repository

import androidx.lifecycle.LiveData
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.local.datasource.FakeFoodDataSourceTest
import com.example.savethefood.data.source.local.datasource.FakeUserDataSourceTest

class FakeFoodDataRepositoryTest(
    private val fakeFoodDataSourceTest: FakeFoodDataSourceTest
) : FoodRepository{

    override suspend fun getApiFoodUpc(barcode: String): Result<FoodDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun saveNewFood(food: FoodDomain): Long {
        TODO("Not yet implemented")
    }

    override suspend fun getFoods(): LiveData<Result<List<FoodDomain>>> {
        return fakeFoodDataSourceTest.getFoods()
    }

    override suspend fun deleteFood(food: FoodDomain?) {
        TODO("Not yet implemented")
    }

}