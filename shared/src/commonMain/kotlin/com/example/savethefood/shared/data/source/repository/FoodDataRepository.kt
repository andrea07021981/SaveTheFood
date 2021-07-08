package com.example.savethefood.shared.data.source.repository

import com.example.savethefood.shared.data.ActionResult
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.data.domain.FoodItem
import com.example.savethefood.shared.data.domain.FoodSearchDomain
import com.example.savethefood.shared.data.domain.asDatabaseModel
import com.example.savethefood.shared.data.source.FoodDataSource
import com.example.savethefood.shared.utils.FoodImage
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.reflect.KProperty1


// TODO we will need to inject the context to vm, repository.
//  In this way the DatabaseDriverFactory will receive the android context and for ios?
/**
 *
 *
 *
FoodDataRepository(
FoodLocalDataSource(
DatabaseFactory(DatabaseDriverFactory(this)).createDatabase()
)
)
 */
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
        val insertFoodId = foodLocalDataSource.insertNewFood(food)
        if (insertFoodId > 0) {
            return@withContext ActionResult.Success(food)
        } else {
            return@withContext ActionResult.Error("Error saving food")
        }
    }

    override fun getFoods(): Flow<ActionResult<List<FoodDomain>>> {
        return foodLocalDataSource.getFoods()
            .onEach {
                check(it != null)
            }
            .map {
                when {
                    it == null -> {
                        ActionResult.Error("No data")
                    }
                    it.isNotEmpty() -> {
                        ActionResult.Success(it)
                    }
                    else -> {
                        ActionResult.Success(emptyList())
                    }
                }
            }.retryWhen {cause, attempt ->
                if (cause is IOException && attempt < 5) {    // retry on IOException
                    delay(1000)                     // delay for one second before retry
                    true
                } else {                                      // do not retry otherwise
                    false
                }
            }
            // FLOWON is the correct way to change the context. The collection remains in main thread, but this flow goes in IO concurrently
            .flowOn(ioDispatcher)
            // We can tell flow to make the buffer "conflated". It removes the buffer from flowOn
            // and only shares the last value, as our UI discards any intermediate values in the
            // buffer.
            .conflate()
    }

    override suspend fun getLocalFoods(): ActionResult<List<FoodDomain>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFood(food: FoodDomain): Long? = withContext(Dispatchers.Default) {
        foodLocalDataSource.deleteFood(food.asDatabaseModel())
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