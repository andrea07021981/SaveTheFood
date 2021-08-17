package com.example.savethefood.shared

import com.example.savethefood.shared.cache.SaveTheFoodDatabase
import com.example.savethefood.shared.data.source.local.database.DatabaseDriverFactory
import com.example.savethefood.shared.data.source.local.database.DatabaseFactory
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class SqlDelightTest : BaseTest() {

    lateinit var database: SaveTheFoodDatabase

    @BeforeTest
    fun setUp() = runTest {
        database = testDbConnection()
    }

    @Test
    fun `Select all food no records`() {
        val foods = database.saveTheFoodDatabaseQueries.selectFoods().executeAsList()
        assertEquals(foods.size, 0)
    }
}