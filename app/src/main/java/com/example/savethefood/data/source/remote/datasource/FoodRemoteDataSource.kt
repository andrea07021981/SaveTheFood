package com.example.savethefood.data.source.remote.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.FoodSearchDomain
import com.example.savethefood.data.source.FoodDataSource
import com.example.savethefood.data.source.local.entity.FoodEntity
import com.example.savethefood.data.source.remote.datatransferobject.asDomainModel
import com.example.savethefood.data.source.remote.service.FoodService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject

class FoodRemoteDataSource @Inject constructor(
    private val foodApi: FoodService
) : FoodDataSource {

    //Callbackflow is a good way to emit values inside listeners, We could use send(), it is asynchronous
    // IMPORTANT, IF WE USE COROUTINE, FOLLOW THIS ONE https://medium.com/androiddevelopers/livedata-with-coroutines-and-flow-part-iii-livedata-and-coroutines-patterns-592485a4a85a
    /*
    suspend fun doOneShot(param: String) : Result<String> =
    suspendCancellableCoroutine { continuation ->
        api.addOnCompleteListener { result ->
            continuation.resume(result)
        }.addOnFailureListener { error ->
            continuation.resumeWithException(error)
        }.fetchSomething(param)
      }
     */

    /**may throw Exception, with coroutineScope is possible Exception will cancell only the coroutines created in
     *This scope, without touching the outer scope. We could avoid it and use the supervisor job in VM, but this way is more efficient
     */
    @Throws(Exception::class)
    override suspend fun getFoodByUpc(barcode: String): Result<FoodDomain> = coroutineScope{
        try {
            val foodData = foodApi.getFoodByUpc(barcode).await()
            Log.d("JSON RESULT", foodData.id.toString())
            return@coroutineScope Result.Success(foodData.asDomainModel())
        } catch (e: Exception) {
            return@coroutineScope Result.ExError(e)
        }
    }

    @Throws(Exception::class)
    override suspend fun getFoodByQuery(query: String): Result<FoodSearchDomain>?  = coroutineScope{
        try {
            val foodData = foodApi.getFoodByName(query = query)
            //Log.d("JSON RESULT", foodData.id.toString())
            return@coroutineScope Result.Success(foodData.asDomainModel())
        } catch (e: Exception) {
            return@coroutineScope Result.ExError(e)
        }
    }

    override suspend fun getFoodById(id: Int): Result<FoodDomain> = coroutineScope{
        try {
            val foodData = foodApi.getFoodById(id)
            return@coroutineScope Result.Success(foodData.asDomainModel())
        } catch (e: Exception) {
            return@coroutineScope Result.Error(e.toString())
        }
    }


    override suspend fun insertFood(food: FoodDomain): Long {
        TODO("No OP")
    }

    override suspend fun updateFoods(food: FoodDomain) {
        TODO("Not yet implemented")
    }

    override suspend fun getFoods(): Flow<List<FoodEntity>> {
        TODO("No OP")
    }

    override suspend fun getLocalFoods(): Result<List<FoodDomain>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFood(food: FoodDomain?): Int {
        TODO("No OP")
    }
}