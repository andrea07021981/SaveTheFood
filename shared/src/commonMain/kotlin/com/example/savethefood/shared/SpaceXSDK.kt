package com.example.savethefood.shared

import com.example.savethefood.shared.data.source.local.database.Database
import com.example.savethefood.shared.data.source.local.database.DatabaseDriverFactory
import com.example.savethefood.shared.entity.FoodEntity


class SpaceXSDK (databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)


    @Throws(Exception::class) suspend fun setFood(food: FoodEntity): List<FoodEntity> {
        val newFood = database.insertFood(food)
        return if (newFood > 0) {
            database.retrieveFoods()
        } else {
            listOf()
        }
    }
}
