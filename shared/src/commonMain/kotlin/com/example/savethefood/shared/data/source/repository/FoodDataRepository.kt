package com.example.savethefood.shared.data.source.repository

import com.example.savethefood.shared.data.ActionResult
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.data.domain.FoodItem
import com.example.savethefood.shared.data.domain.FoodSearchDomain
import com.example.savethefood.shared.data.source.FoodDataSource
import com.example.savethefood.shared.utils.FoodImage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KProperty1

@ExperimentalCoroutinesApi
class FoodDataRepository (
    private val foodLocalDataSource: FoodDataSource,
    //private val foodRemoteDataSource: FoodDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : FoodRepository {

    @Throws(Exception::class)
    override suspend fun getApiFoodUpc(barcode: String): ActionResult<FoodDomain> = coroutineScope{
        TODO("Not yet implemented")
    }

    @Throws(Exception::class)
    override suspend fun getApiFoodQuery(query: String): ActionResult<FoodSearchDomain>? = coroutineScope{
        TODO("Not yet implemented")
    }

    @Throws(Exception::class)
    override suspend fun getApiFoodById(id: Int): ActionResult<FoodDomain> = coroutineScope{
        TODO("Not yet implemented")
    }

    @Throws(Exception::class)
    override suspend fun refreshData() = coroutineScope{
        TODO("Not yet implemented")
    }

    override suspend fun saveNewFood(food: FoodDomain): ActionResult<FoodDomain> = withContext(Dispatchers.Default) {
        TODO("Not yet implemented")
    }

    override fun getFoods(): Flow<ActionResult<List<FoodDomain>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getLocalFoods(): ActionResult<List<FoodDomain>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFood(food: FoodDomain): Int  = withContext(Dispatchers.Default) {
        TODO("Not yet implemented")
    }

    /**
     * Create a LinkedHashset, we don't have duplicates but must keep the order
     */
    override fun getFoodImages(orderField: KProperty1<FoodImage, String>): LinkedHashSet<FoodItem> {
        val customObjects = linkedSetOf<FoodItem>()
        customObjects
            .apply {
                FoodImage.values()
                    .sortedBy(orderField)
                    .forEach {
                        this.add(FoodItem(it.name, it))
                    }
            }
        return customObjects
    }
}