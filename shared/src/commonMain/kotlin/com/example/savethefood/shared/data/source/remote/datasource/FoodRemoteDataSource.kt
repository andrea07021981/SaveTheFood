package com.example.savethefood.shared.data.source.remote.datasource

import com.example.savethefood.shared.data.Result
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.data.domain.FoodSearchDomain
import com.example.savethefood.shared.data.source.FoodDataSource
import com.example.savethefood.shared.data.source.local.entity.FoodEntity
import com.example.savethefood.shared.data.source.remote.datatransferobject.asDomainModel
import com.example.savethefood.shared.data.source.remote.service.FoodServiceApi
import io.ktor.client.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow


// TODO pass params with DI
class FoodRemoteDataSource(
    private var client: FoodServiceApi = FoodServiceApi()
) : FoodDataSource {

    /**may throw Exception, with coroutineScope is possible Exception will cancell only the coroutines created in
     *This scope, without touching the outer scope. We could avoid it and use the supervisor job in VM, but this way is more efficient
     */
    @Deprecated("No network food")
    @Throws(Exception::class)
    override suspend fun getFoodByUpc(barcode: String): Result<FoodDomain> = coroutineScope{
        /*try {
            val foodData = foodApi.getFoodByUpc(barcode).await()
            Log.d("JSON RESULT", foodData.id.toString())
            return@coroutineScope Result.Success(foodData.asDomainModel())
        } catch (e: Exception) {
            return@coroutineScope Result.ExError(e)
        }*/
        return@coroutineScope Result.Success(FoodDomain())
    }

    @Throws(Exception::class)
    override suspend fun getFoodByQuery(barcode: String): Result<FoodSearchDomain>?  = coroutineScope{
        try {
            val foodData = client.getFoodByName(query = barcode)
            //Log.d("JSON RESULT", foodData.id.toString())
            return@coroutineScope Result.Success(foodData.asDomainModel())
        } catch (e: Exception) {
            return@coroutineScope Result.ExError(e)
        }
    }

    @Deprecated("No network now")
    override suspend fun getFoodById(id: Int): Result<FoodDomain> = coroutineScope{
        try {
            val foodData = client.getFoodById(id)
            return@coroutineScope Result.Success(foodData.asDomainModel())
        } catch (e: Exception) {
            return@coroutineScope Result.Error(e.toString())
        }
    }

    override suspend fun updateFoods(food: FoodDomain) {
        TODO("Not yet implemented")
    }


    override suspend fun insertNewFood(food: FoodDomain): Long {
        TODO("No OP")
    }

    override fun getFoods(): Flow<List<FoodDomain>> {
        TODO("No OP")
    }

    override suspend fun getLocalFoods(): Result<List<FoodDomain>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFood(food: FoodEntity): Long? {
        TODO("No OP")
    }
}