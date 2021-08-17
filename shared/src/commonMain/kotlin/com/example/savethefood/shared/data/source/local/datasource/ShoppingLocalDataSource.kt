package com.example.savethefood.shared.data.source.local.datasource

import com.example.savethefood.shared.cache.SaveTheFoodDatabase
import com.example.savethefood.shared.cache.SaveTheFoodDatabaseQueries
import com.example.savethefood.shared.data.domain.BagDomain
import com.example.savethefood.shared.data.domain.asDatabaseModel
import com.example.savethefood.shared.data.source.ShoppingDataSource
import com.example.savethefood.shared.data.source.local.entity.BagEntity
import com.example.savethefood.shared.data.source.local.entity.RecipeIngredientEntity
import com.example.savethefood.shared.data.source.local.entity.asDomainModel
import com.example.savethefood.shared.data.source.local.entity.asRecipeDomainModel
import com.example.savethefood.shared.utils.FoodImage
import com.example.savethefood.shared.utils.QuantityType
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ShoppingLocalDataSource(
    private val foodDatabase: SaveTheFoodDatabase
) : ShoppingDataSource {

    private val dbQuery: SaveTheFoodDatabaseQueries = foodDatabase.saveTheFoodDatabaseQueries

    override fun getFoodsInBag(): Flow<List<BagDomain>?> {
        return dbQuery.selectBags(::mapToBagEntity).asFlow().mapToList().asDomainModel()
    }

    override suspend fun saveItemInBag(item: BagDomain): Long {
        return item.asDatabaseModel().run {
            dbQuery.transactionWithResult {
                dbQuery.insertBag(
                    id = id.toLong(),
                    title = title,
                    img = img,
                    quantityType = quantityType,
                    quantity = quantity,
                )
                dbQuery.lastInsertRowId().executeAsOne()
            }
        }
    }

    private fun mapToBagEntity(
        id: Long,
        title: String,
        img: FoodImage,
        quantityType: QuantityType,
        quantity: Double?,
    ): BagEntity {
        return BagEntity(
            id = id.toInt(),
            title = title,
            img = img,
            quantityType = quantityType,
            quantity = quantity
        )
    }
}