package com.example.savethefood.shared.data.source.local.datasource

import com.example.savethefood.shared.cache.SaveTheFoodDatabase
import com.example.savethefood.shared.cache.SaveTheFoodDatabaseQueries
import com.example.savethefood.shared.data.Result
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.data.domain.FoodSearchDomain
import com.example.savethefood.shared.data.domain.asDatabaseModel
import com.example.savethefood.shared.data.source.FoodDataSource
import com.example.savethefood.shared.data.source.local.entity.FoodEntity
import com.example.savethefood.shared.data.source.local.entity.asDomainModel
import com.example.savethefood.shared.utils.FoodImage
import com.example.savethefood.shared.utils.QuantityType
import com.example.savethefood.shared.utils.StorageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class FoodLocalDataSource constructor(
    private val foodDatabase: SaveTheFoodDatabase
) : FoodDataSource {

    private val dbQuery: SaveTheFoodDatabaseQueries = foodDatabase.saveTheFoodDatabaseQueries

    override suspend fun getFoodByUpc(barcode: String): Result<FoodDomain> {
        TODO("No OP")
    }

    override suspend fun getFoodByQuery(barcode: String): Result<FoodSearchDomain>? {
        TODO("No OP")
    }

    override suspend fun getFoodById(id: Int): Result<FoodDomain> {
        TODO("Not yet implemented")
    }

    // TODO there is no way to update clearly with the current version, wait new releases
    override suspend fun updateFoods(food: FoodDomain) {
        insertNewFood(food)
    }

    override suspend fun insertNewFood(food: FoodDomain): Long {
        val entity = food.asDatabaseModel()
        return dbQuery.transactionWithResult {
            dbQuery.insertFood(
                id = entity.id,
                title = entity.title,
                description = entity.description,
                img = entity.img,
                price = entity.price,
                quantityType = entity.quantityType,
                quantity = entity.quantity,
                storageType = entity.storageType,
                bestBefore = entity.bestBefore,
                lastUpdate = entity.lastUpdate
            )
            dbQuery.lastInsertRowId().executeAsOne()
        }
    }

    @Throws(Exception::class)
    override fun getFoods(): Flow<List<FoodDomain>?> {
        return flowOf(
            dbQuery.selectFoods(::mapToFoodEntity).executeAsList().asDomainModel()
        )
    }

    private fun mapToFoodEntity(
        id: Long,
        title: String,
        description: String?,
        img: FoodImage,
        price: Double?,
        quantityType: QuantityType,
        quantity: Double?,
        storageType: StorageType,
        best: Long,
        last: Long
    ): FoodEntity {
        return FoodEntity(
            id = id,
            title = title,
            description = description,
            img = img,
            price = price,
            quantityType = quantityType,
            quantity = quantity,
            storageType = storageType,
            bestBefore = best,
            lastUpdate = last
        )
    }

    override suspend fun getLocalFoods(): Result<List<FoodDomain>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFood(food: FoodEntity): Long? {
        return withContext(Dispatchers.Default) {
            dbQuery.transactionWithResult {
                dbQuery.deleteFoodById(food.id)
                dbQuery.changes().executeAsOneOrNull()
            }
        }
    }
}