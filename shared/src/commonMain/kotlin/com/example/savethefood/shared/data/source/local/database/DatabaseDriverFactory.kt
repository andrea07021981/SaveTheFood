package com.example.savethefood.shared.data.source.local.database

import com.squareup.sqldelight.db.SqlDriver

// Common Code
@Suppress("NO_ACTUAL_FOR_EXPECT") // TODO REMOVE IT, BUG
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}