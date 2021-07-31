package com.example.savethefood.shared.di


// TODO ad koin but wait the sqldelight injection. For now manual DI in the module
/*
fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(commonUserModule)
}

// called by iOS etc
fun initKoin() = initKoin{}

val commonUserModule = module {
    single { DatabaseDriverFactory }
    single { DatabaseFactory(databaseDriverFactory = databaseDriverFactory).createDatabase() }
    single { UserLocalDataSource(get()) }
}
*/

