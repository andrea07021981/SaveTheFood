package com.example.savethefood.di

import android.content.Context
import com.example.savethefood.shared.Greeting
import com.example.savethefood.shared.data.source.local.database.DatabaseDriverFactory
import com.example.savethefood.shared.data.source.local.database.DatabaseFactory
import com.example.savethefood.shared.data.source.local.datasource.FoodLocalDataSource
import com.example.savethefood.shared.data.source.local.datasource.UserLocalDataSource
import com.example.savethefood.shared.data.source.remote.datasource.UserRemoteDataSource
import com.example.savethefood.shared.data.source.repository.FoodDataRepository
import com.example.savethefood.shared.data.source.repository.UserDataRepository

/**
 * TODO Temporary class for manual DI. I need to decide whether using hilt or koin.
 * HILT: Not easy to use in the shared module due to some lacks in the dependencies. Official sites
 *       offers an example of module dependencies but it does not work
 * KOIN: Best for module, but there's still a problem with the database factory
 */
object DIBuilder {

    fun getSharedUserRepository(context: Context): UserDataRepository = UserDataRepository(
        UserLocalDataSource(
            DatabaseFactory(DatabaseDriverFactory(context)).createDatabase()
        ),
        UserRemoteDataSource()
    )
}