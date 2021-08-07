package com.example.savethefood.shared.di

import com.example.savethefood.shared.data.source.UserDataSource
import com.example.savethefood.shared.data.source.local.database.DatabaseDriverFactory
import com.example.savethefood.shared.data.source.local.database.DatabaseFactory
import com.example.savethefood.shared.data.source.local.datasource.UserLocalDataSource
import com.example.savethefood.shared.data.source.remote.datasource.UserRemoteDataSource
import com.example.savethefood.shared.data.source.repository.UserDataRepository
import com.example.savethefood.shared.data.source.repository.UserRepository
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


// TODO Need to be fixed the named interfaces, return error
fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    //modules(commonUserModule)
}

// called by iOS etc
fun initKoin() = initKoin{}

val commonUserModule = module {
    single { DatabaseDriverFactory() }
    single { DatabaseFactory(databaseDriverFactory = get()).createDatabase() }

    single<UserDataSource>(named("userLocalDataSource")) { UserLocalDataSource(get()) }
    single<UserDataSource>(named("userRemoteDataSource")) { UserRemoteDataSource() }
    single<UserRepository> { UserDataRepository(userLocalDataSource = get(), userRemoteDataSource = get()) }
}


