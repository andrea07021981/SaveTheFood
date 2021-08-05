package com.example.savethefood.shared.di

import com.example.savethefood.shared.data.source.local.database.DatabaseDriverFactory
import com.example.savethefood.shared.data.source.local.database.DatabaseFactory
import com.example.savethefood.shared.data.source.local.datasource.UserLocalDataSource
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


// TODO ad koin but wait the sqldelight injection. For now manual DI in the module
fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(commonUserModule)
}

// called by iOS etc
fun initKoin() = initKoin{}

val commonUserModule = module {
    /*single { DatabaseDriverFactory() }
    single { DatabaseFactory(databaseDriverFactory = databaseDriverFactory).createDatabase() }
    single { UserLocalDataSource(get()) }*/
}


