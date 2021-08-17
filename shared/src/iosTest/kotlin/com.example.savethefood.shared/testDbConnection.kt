package com.example.savethefood.shared

import co.touchlab.sqliter.DatabaseConfiguration
import com.example.savethefood.shared.cache.SaveTheFoodDatabase
import com.example.savethefood.shared.data.source.local.database.DatabaseDriverFactory
import com.example.savethefood.shared.data.source.local.database.DatabaseFactory
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.squareup.sqldelight.drivers.native.wrapConnection

actual fun testDbConnection() : SaveTheFoodDatabase {
    return DatabaseFactory(
        DatabaseDriverFactory()
    ).createDatabase()
}