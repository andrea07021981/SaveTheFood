package com.example.savethefood.data.source.local.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.asDatabaseModel
import com.example.savethefood.data.source.FoodDataSource
import com.example.savethefood.data.source.local.dao.FoodDatabaseDao
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodSearchDomain
import com.example.savethefood.data.source.local.entity.asDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FoodLocalDataSource  @Inject constructor(
    private val foodDatabaseDao: FoodDatabaseDao
) : FoodDataSource {

    override suspend fun getFoodByUpc(barcode: String): Result<FoodDomain> {
        TODO("No OP")
    }

    override suspend fun getFoodByQuery(barcode: String): Result<FoodSearchDomain>? {
        TODO("No OP")
    }

    override suspend fun getFoodById(id: Int): Result<FoodDomain> {
        TODO("Not yet implemented")
    }

    /**
    This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     * ----------------------------------------------------------------------------
     * Withcontext is a function that allows to easily change the context that will be used to run a part of the code inside a coroutine.
     * This is a suspending function, so it means that it’ll suspend the coroutine until the code inside is executed, no matter the dispatcher that it’s used.
     * With that, we can make our suspending functions use a different thread:
     */
    override suspend fun insertFood(food: FoodDomain) = withContext(Dispatchers.IO) {
        foodDatabaseDao.insert(food.asDatabaseModel())
    }

    //TODO use varargs as dev-bytes
    override suspend fun updateFoods(food: FoodDomain) {
        foodDatabaseDao.updateAll(food.asDatabaseModel())
    }

    override suspend fun getFoods(): LiveData<Result<List<FoodDomain>>> {
        //TODO add try catch with Result.ExError
        return foodDatabaseDao.observeFoods().map {
            Result.Success(it.asDomainModel())
        }
    }

    override suspend fun getLocalFoods(): Result<List<FoodDomain>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFood(food: FoodDomain?): Int {
        return withContext(Dispatchers.IO) {
            foodDatabaseDao.deleteFood(food = food!!.asDatabaseModel())
        }
    }
}