package com.example.savethefood.shared

import com.example.savethefood.shared.data.source.local.database.DatabaseFactory
import com.example.savethefood.shared.data.source.local.database.DatabaseDriverFactory
import com.example.savethefood.shared.data.source.local.entity.FoodEntity


class SpaceXSDK (databaseDriverFactory: DatabaseDriverFactory) {
    private val database =
        DatabaseFactory(databaseDriverFactory = databaseDriverFactory).createDatabase()
/*

    @Throws(Exception::class) suspend fun setFood(food: FoodEntity): List<FoodEntity> {
        val newFood = database.insertFood(food)
        return if (newFood > 0) {
            database.retrieveFoods()
        } else {
            listOf()
        }
    }*/
}
