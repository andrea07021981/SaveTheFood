package com.example.savethefood.shared

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.savethefood.shared.cache.SaveTheFoodDatabase
import com.example.savethefood.shared.data.source.local.database.DatabaseDriverFactory
import com.example.savethefood.shared.data.source.local.database.DatabaseFactory

actual fun testDbConnection() : SaveTheFoodDatabase {
    val app = ApplicationProvider.getApplicationContext<Application>()
    return DatabaseFactory(
        DatabaseDriverFactory(app)
    ).createDatabase()
}