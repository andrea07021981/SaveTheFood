package com.example.savethefood.shared

import com.example.savethefood.shared.cache.SaveTheFoodDatabase
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.data.source.local.database.DatabaseDriverFactory
import com.example.savethefood.shared.data.source.local.database.DatabaseFactory
import com.example.savethefood.shared.utils.FoodImage
import com.example.savethefood.shared.utils.QuantityType
import com.example.savethefood.shared.utils.StorageType
import io.ktor.util.date.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

//class SqlDelightTest : BaseTest() {

    // TODO issue with gradlew build, wait new kmm release
    /*lateinit var database: SaveTheFoodDatabase

    @BeforeTest
    fun setUp() = runTest {
        database = testDbConnection()
    }

    @Test
    fun selectAllFoodNoRecords() {
        val foods = database.saveTheFoodDatabaseQueries.selectFoods().executeAsList()
        assertEquals(foods.size, 0)
    }

    @Test
    fun addFood() {
        addTempFood()
        val foodId = database.saveTheFoodDatabaseQueries.selectFoodById(1).executeAsOne()
        assertEquals(foodId.title, "title")
    }

    @Test
    fun updateFood() {
        addTempFood()
        val food = database.saveTheFoodDatabaseQueries.selectFoodById(1).executeAsOne()
        database.saveTheFoodDatabaseQueries.updateFood(
            1,
            "new title",
            "desc",
            FoodImage.EMPTY,
            12.0,
            QuantityType.UNIT,
            2.0,
            StorageType.DRY,
            GMTDate().timestamp,
            GMTDate().timestamp,
            1
        )
        val updatedFood = database.saveTheFoodDatabaseQueries.selectFoodById(1).executeAsOne()
        assertNotEquals(updatedFood.title, food.title)
    }

    @Test
    fun deleteFood() {
        addTempFood()
        database.saveTheFoodDatabaseQueries.deleteFoodById(1)
        val foodId = database.saveTheFoodDatabaseQueries.selectFoodById(1).executeAsOneOrNull()?.id
        assertNotEquals(foodId, 1)
    }

    private fun addTempFood() {
        database.saveTheFoodDatabaseQueries.insertFood(
            1,
            "title",
            "desc",
            FoodImage.EMPTY,
            12.0,
            QuantityType.UNIT,
            2.0,
            StorageType.DRY,
            GMTDate().timestamp,
            GMTDate().timestamp
        )
    }*/
//}