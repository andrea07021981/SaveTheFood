package com.example.savethefood.shared.data.source.repository

import com.example.savethefood.shared.data.Result
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

// TODO remove the @Throws(Exception::class) from the class implementation, they are already declared into the interfaces
@ExperimentalCoroutinesApi
class FoodDataRepository (
    private val foodLocalDataSource: FoodDataSource,
    private val foodRemoteDataSource: FoodDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : FoodRepository {

    @Throws(Exception::class)
    override suspend fun getApiFoodUpc(barcode: String): Result<FoodDomain> = coroutineScope{
        val foodRetrieved = foodRemoteDataSource.getFoodByUpc(barcode)
        if (foodRetrieved is Result.Success) {
            val saveNewFood = foodLocalDataSource.insertNewFood(foodRetrieved.data)
            if (saveNewFood == 0L) {
                return@coroutineScope Result.Error("Not saved")
            } else {
                return@coroutineScope Result.Success(foodRetrieved.data)
            }
        } else {
            return@coroutineScope foodRetrieved
        }
    }

    @Throws(Exception::class)
    override suspend fun getApiFoodQuery(query: String): Result<FoodSearchDomain>? = coroutineScope{
        foodRemoteDataSource.getFoodByQuery(query)
    }

    @Throws(Exception::class)
    override suspend fun getApiFoodById(id: Int): Result<FoodDomain> = coroutineScope{
        val foodByIdResult = foodRemoteDataSource.getFoodById(id)
        if (foodByIdResult is Result.Success) {
            foodLocalDataSource.insertNewFood(foodByIdResult.data)
            return@coroutineScope foodByIdResult
        } else {
            throw Exception(foodByIdResult.toString())
        }
    }

    @Throws(Exception::class)
    override suspend fun refreshData() = coroutineScope{
        val localFoods = foodLocalDataSource.getLocalFoods()
        if (localFoods is Result.Success) {
            for (food in localFoods.data) {
                val foodById = foodRemoteDataSource.getFoodById(food.id.toInt())
                if (foodById is Result.Success) {
                    foodLocalDataSource.updateFoods(foodById.data)
                }
            }
        } else{
            throw Exception(localFoods.toString())
        }
    }

    override suspend fun saveNewFood(food: FoodDomain): Result<FoodDomain> = withContext(Dispatchers.Default) {
        val insertFoodId = foodLocalDataSource.insertNewFood(food)
        if (insertFoodId > 0) {
            return@withContext Result.Success(food)
        } else {
            return@withContext Result.Error("Error saving food")
        }
    }

    override fun getFoods(): Flow<Result<List<FoodDomain>>> {
        return foodLocalDataSource.getFoods()
            .onEach {
                check(it != null)
            }
            .map {
                when {
                    it == null -> {
                        Result.Error("No data")
                    }
                    it.isNotEmpty() -> {
                        Result.Success(it)
                    }
                    else -> {
                        Result.Success(emptyList())
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
            // Emits only if any item in room has changed. Non needed for now, the query always extracts all food
            //.distinctUntilChanged()
            // FLOWON is the correct way to change the context. The collection remains in main thread, but this flow goes in IO concurrently
            .flowOn(ioDispatcher)
            // We can tell flow to make the buffer "conflated". It removes the buffer from flowOn
            // and only shares the last value, as our UI discards any intermediate values in the
            // buffer.
            .conflate()
    }

    override suspend fun getLocalFoods(): Result<List<FoodDomain>> {
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