package com.example.savethefood.shared.data.source.remote.datasource

import com.example.savethefood.shared.data.ActionResult
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.data.domain.FoodSearchDomain
import com.example.savethefood.shared.data.source.FoodDataSource
import com.example.savethefood.shared.data.source.remote.service.KtorClientFactory
import io.ktor.client.*
import kotlinx.coroutines.coroutineScope


/*
// TODO pass params with DI
class FoodRemoteDataSource(
    private var client: HttpClient = KtorClientFactory().build()
) : FoodDataSource {

    //Callbackflow is a good way to emit values inside listeners, We could use send(), it is asynchronous
    // Refrofit do not need it anymore since they implemented coroutines, but use suspendCoroutine for flows
    // and suspendCancellableCoroutine for one shot with api that do not have coroutines
    // IMPORTANT, IF WE USE COROUTINE, FOLLOW THIS ONE https://medium.com/androiddevelopers/livedata-with-coroutines-and-flow-part-iii-livedata-and-coroutines-patterns-592485a4a85a
    */
/*
    suspend fun doOneShot(param: String) : Result<String> =
    suspendCancellableCoroutine { continuation ->
        api.addOnCompleteListener { result ->
            continuation.resume(result)
        }.addOnFailureListener { error ->
            continuation.resumeWithException(error)
        }.fetchSomething(param)
      }
     *//*


    */
/**may throw Exception, with coroutineScope is possible Exception will cancell only the coroutines created in
     *This scope, without touching the outer scope. We could avoid it and use the supervisor job in VM, but this way is more efficient
     *//*

    @Deprecated("No network food")
    @Throws(Exception::class)
    override suspend fun getFoodByUpc(barcode: String): ActionResult<FoodDomain> = coroutineScope{
        */
/*try {
            val foodData = foodApi.getFoodByUpc(barcode).await()
            Log.d("JSON RESULT", foodData.id.toString())
            return@coroutineScope Result.Success(foodData.asDomainModel())
        } catch (e: Exception) {
            return@coroutineScope Result.ExError(e)
        }*//*

        return@coroutineScope ActionResult.Success(FoodDomain())
    }

    @Throws(Exception::class)
    override suspend fun getFoodByQuery(barcode: String): ActionResult<FoodSearchDomain>?  = coroutineScope{
        */
/*try {
            val foodData = foodApi.getFoodByName(query = barcode)
            //Log.d("JSON RESULT", foodData.id.toString())
            return@coroutineScope Result.Success(foodData.asDomainModel())
        } catch (e: Exception) {
            return@coroutineScope Result.ExError(e)
        }*//*

    }

    @Deprecated("No network now")
    override suspend fun getFoodById(id: Int): Result<FoodDomain> = coroutineScope{
        */
/*try {
            val foodData = foodApi.getFoodById(id)
            return@coroutineScope Result.Success(foodData.asDomainModel())
        } catch (e: Exception) {
            return@coroutineScope Result.Error(e.toString())
        }*//*

        return@coroutineScope Result.Success(FoodDomain())
    }


    override suspend fun insertFood(food: FoodDomain): Long {
        TODO("No OP")
    }

    override suspend fun updateFoods(food: FoodDomain) {
        TODO("Not yet implemented")
    }

    override fun getFoods(): Flow<List<FoodEntity>> {
        TODO("No OP")
    }

    override suspend fun getLocalFoods(): Result<List<FoodDomain>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFood(food: FoodEntity): Int {
        TODO("No OP")
    }
}*/
