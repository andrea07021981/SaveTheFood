package com.example.savethefood.data.source.remote.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.FoodDataSource
import com.example.savethefood.data.source.remote.datatransferobject.asDomainModel
import com.example.savethefood.data.source.remote.service.ApiClient
import com.example.savethefood.data.source.remote.service.FoodService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import java.lang.Exception

class FoodRemoteDataSource(
    private val foodApi: FoodService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FoodDataSource {

    /**may throw Exception, with coroutineScope is possible Exception will cancell only the coroutines created in
     *This scope, without touching the outer scope. We could avoid it and use the supervisor job in VM, but this way is more efficient
     */
    @Throws(Exception::class)
    override suspend fun getFoodByUpc(barcode: String): Result<FoodDomain?> = coroutineScope{
        try {
            val foodData = foodApi.getFoodByUpc(barcode).await()
            Log.d("JSON RESULT", foodData.id.toString())
            return@coroutineScope Result.Success(foodData.asDomainModel())
        } catch (e: Exception) {
            return@coroutineScope Result.ExError(e)
        }
    }

    override suspend fun saveNewFood(food: FoodDomain) {
        TODO("No OP")
    }

    override suspend fun getFoods(): LiveData<List<FoodDomain>> {
        TODO("No OP")
    }

    override suspend fun deleteFood(food: FoodDomain?) {
        TODO("No OP")
    }
}