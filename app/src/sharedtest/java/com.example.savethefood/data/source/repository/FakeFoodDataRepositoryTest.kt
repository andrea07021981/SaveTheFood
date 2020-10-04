package com.example.savethefood.data.source.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.FoodSearchDomain
import com.example.savethefood.data.source.FoodDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class FakeFoodDataRepositoryTest(
    private val fakeRemoteFoodDataSourceTest: FoodDataSource,
    private val fakeLocalFoodDataSourceTest: FoodDataSource
) : FoodRepository{
    override suspend fun getApiFoodUpc(barcode: String): Result<FoodDomain> = coroutineScope {
        val foodRetrieved = fakeRemoteFoodDataSourceTest.getFoodByUpc(barcode)
        if (foodRetrieved is Result.Success) {
            val saveNewFood = fakeLocalFoodDataSourceTest.insertFood(foodRetrieved.data)
            if (saveNewFood == 0L) {
                return@coroutineScope Result.Error("Not saved")
            } else {
                return@coroutineScope Result.Success(foodRetrieved.data)
            }
        } else {
            return@coroutineScope Result.Error("Not retrieved")
        }
    }

    override suspend fun getApiFoodQuery(query: String): Result<FoodSearchDomain>? = coroutineScope {
        fakeRemoteFoodDataSourceTest.getFoodByQuery(query)
    }

    override suspend fun getApiFoodById(id: Int): Result<FoodDomain> {
        fakeLocalFoodDataSourceTest.getFoodById(id).apply {
            return if (this is Result.Success) {
                this
            } else{
                fakeRemoteFoodDataSourceTest.getFoodById(id)
            }
        }
    }

    override suspend fun refreshData() {
        TODO("Not yet implemented")
    }

    override suspend fun saveNewFood(food: FoodDomain): Long = withContext(Dispatchers.IO) {
        fakeLocalFoodDataSourceTest.insertFood(food)
    }

    override suspend fun getFoods(): LiveData<Result<List<FoodDomain>>> {
        return fakeLocalFoodDataSourceTest.getFoods()
    }

    override suspend fun getLocalFoods(): Result<List<FoodDomain>> {
        return fakeLocalFoodDataSourceTest.getLocalFoods()
    }

    override suspend fun deleteFood(food: FoodDomain?): Int {
        return fakeLocalFoodDataSourceTest.deleteFood(food)
    }
}