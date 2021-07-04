package com.example.savethefood.shared.data.source.local.database

import com.example.savethefood.shared.cache.Food
import com.example.savethefood.shared.cache.SaveTheFoodDatabase
import com.example.savethefood.shared.data.source.local.entity.FoodEntity
import com.example.savethefood.shared.utils.FoodImage
import com.example.savethefood.shared.utils.QuantityType
import com.example.savethefood.shared.utils.StorageType
import com.squareup.sqldelight.EnumColumnAdapter

// TODO inject databaseDriverFactory, remove from here
internal class DatabaseFactory(
    val databaseDriverFactory: DatabaseDriverFactory
) {

    fun createDatabase(): SaveTheFoodDatabase {
        return SaveTheFoodDatabase(
            databaseDriverFactory.createDriver(),
            Food.Adapter(
                imgAdapter = EnumColumnAdapter(),
                quantityTypeAdapter = EnumColumnAdapter(),
                storageTypeAdapter = EnumColumnAdapter()
            )
        )
    }
}