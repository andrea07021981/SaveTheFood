package com.example.savethefood.shared

import com.example.savethefood.shared.cache.Database
import com.example.savethefood.shared.cache.DatabaseDriverFactory
import com.example.savethefood.shared.entity.RocketLaunch


class SpaceXSDK (databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)

    @Throws(Exception::class) suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            listOf()
        }
    }
}
