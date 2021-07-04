package com.example.savethefood.shared.data.source.local.database

import com.example.savethefood.shared.cache.Food
import com.example.savethefood.shared.cache.SaveTheFoodDatabase
import com.example.savethefood.shared.entity.FoodEntity
import com.example.savethefood.shared.utils.FoodImage
import com.example.savethefood.shared.utils.QuantityType
import com.example.savethefood.shared.utils.StorageType
import com.squareup.sqldelight.EnumColumnAdapter

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = SaveTheFoodDatabase(
        databaseDriverFactory.createDriver(),
        Food.Adapter(
            imgAdapter = EnumColumnAdapter(),
            quantityTypeAdapter = EnumColumnAdapter(),
            storageTypeAdapter = EnumColumnAdapter()
        )
    )
    private val dbQuery = database.saveTheFoodDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllFood()
        }
    }

    internal fun insertFood(food: FoodEntity): Long {
        return dbQuery.transactionWithResult {
            dbQuery.insertFood(
                id = food.id.toLong(),
                title = food.title,
                description = food.description,
                img = food.img,
                price = food.price,
                quantityType = food.quantityType,
                quantity = food.quantity,
                storageType = food.storageType,
                bestBefore = food.bestBefore,
                lastUpdate = food.lastUpdate
            )
            dbQuery.lastInsertRowId().executeAsOne()
        }
    }

    internal fun retrieveFoods(): List<FoodEntity> {
        return dbQuery.selectFoods(::mapToFoodEntity).executeAsList()
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
            id = id.toLong(),
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
    /**
     * img TEXT AS FoodImage NOT NULL,
    price REAL,
    quantityType TEXT AS QuantityType NOT NULL,
    quantity REAL,
    storageType TEXT AS StorageType NOT NULL,
    bestBefore INTEGER NOT NULL,
    lastUpdate INTEGER NOT NULL
     */
    /*internal fun getAllLaunches(): List<RocketLaunch> {
        return dbQuery.selectAllLaunchesInfo(::mapLaunchSelecting).executeAsList()
    }

    private fun mapLaunchSelecting(
        flightNumber: Long,
        missionName: String,
        launchYear: Int,
        rocketId: String,
        details: String?,
        launchSuccess: Boolean?,
        launchDateUTC: String,
        missionPatchUrl: String?,
        articleUrl: String?,
        rocket_id: String?,
        name: String?,
        type: String?
    ): RocketLaunch {
        return RocketLaunch(
            flightNumber = flightNumber.toInt(),
            missionName = missionName,
            launchYear = launchYear,
            details = details,
            launchDateUTC = launchDateUTC,
            launchSuccess = launchSuccess,
            rocket = Rocket(
                id = rocketId,
                name = name!!,
                type = type!!
            ),
            links = Links(
                missionPatchUrl = missionPatchUrl,
                articleUrl = articleUrl
            )
        )
    }

    internal fun createLaunches(launches: List<RocketLaunch>) {
        dbQuery.transaction {
            launches.forEach { launch ->
                val rocket = dbQuery.selectRocketById(launch.rocket.id).executeAsOneOrNull()
                if (rocket == null) {
                    insertRocket(launch)
                }

                insertLaunch(launch)
            }
        }
    }

    private fun insertRocket(launch: RocketLaunch) {
        dbQuery.insertRocket(
            id = launch.rocket.id,
            name = launch.rocket.name,
            type = launch.rocket.type
        )
    }

    private fun insertLaunch(launch: RocketLaunch) {
        dbQuery.insertLaunch(
            flightNumber = launch.flightNumber.toLong(),
            missionName = launch.missionName,
            launchYear = launch.launchYear,
            rocketId = launch.rocket.id,
            details = launch.details,
            launchSuccess = launch.launchSuccess ?: false,
            launchDateUTC = launch.launchDateUTC,
            missionPatchUrl = launch.links.missionPatchUrl,
            articleUrl = launch.links.articleUrl
        )
    }*/
}