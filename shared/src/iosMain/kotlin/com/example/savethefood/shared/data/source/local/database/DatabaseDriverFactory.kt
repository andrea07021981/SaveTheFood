package com.example.savethefood.shared.data.source.local.database

import com.example.savethefood.shared.cache.SaveTheFoodDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(SaveTheFoodDatabase.Schema, "test.db")
    }
}