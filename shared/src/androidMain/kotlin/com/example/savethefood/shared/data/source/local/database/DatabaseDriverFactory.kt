package com.example.savethefood.shared.data.source.local.database

import android.content.Context
import com.example.savethefood.shared.cache.SaveTheFoodDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory actual constructor() {

    private lateinit var context: Context

    constructor(ctx: Context) : this() {
        context = ctx
    }
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(SaveTheFoodDatabase.Schema, context, "test.db")
    }
}