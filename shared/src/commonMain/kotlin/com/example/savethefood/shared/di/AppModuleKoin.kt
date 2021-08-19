package com.example.savethefood.shared.di

import com.example.savethefood.shared.data.source.FoodDataSource
import com.example.savethefood.shared.data.source.RecipeDataSource
import com.example.savethefood.shared.data.source.UserDataSource
import com.example.savethefood.shared.data.source.local.database.DatabaseFactory
import com.example.savethefood.shared.data.source.local.datasource.FoodLocalDataSource
import com.example.savethefood.shared.data.source.local.datasource.RecipeLocalDataSource
import com.example.savethefood.shared.data.source.local.datasource.UserLocalDataSource
import com.example.savethefood.shared.data.source.remote.datasource.FoodRemoteDataSource
import com.example.savethefood.shared.data.source.remote.datasource.RecipeRemoteDataSource
import com.example.savethefood.shared.data.source.remote.datasource.UserRemoteDataSource
import com.example.savethefood.shared.data.source.remote.service.FoodServiceApi
import com.example.savethefood.shared.data.source.repository.*
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


// TODO Need to be fixed the named interfaces, return error
fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(listOf(platformModule, commonUserModule, commonFoodModule, commonRecipeModule))
}

// called by iOS
fun initKoin() = initKoin{}

val commonUserModule = module {
    //single { DatabaseDriverFactory() }
    single { DatabaseFactory(databaseDriverFactory = get()).createDatabase() }

    single<UserDataSource>(named("userLocalDataSource")) { UserLocalDataSource(get()) }
    single<UserDataSource>(named("userRemoteDataSource")) { UserRemoteDataSource() }
    single<UserRepository> {
        UserDataRepository(
            userLocalDataSource = get(named("userLocalDataSource")),
            userRemoteDataSource = get(named("userRemoteDataSource"))
        )
    }
}

val commonFoodModule = module {
    single { DatabaseFactory(databaseDriverFactory = get()).createDatabase() }
    single { FoodServiceApi() }
    single<FoodDataSource>(named("foodLocalDataSource")) { FoodLocalDataSource(get()) }
    single<FoodDataSource>(named("foodRemoteDataSource")) { FoodRemoteDataSource(get()) }
    single<FoodRepository> {
        FoodDataRepository(
            foodLocalDataSource = get(named("foodLocalDataSource")),
            foodRemoteDataSource = get(named("foodRemoteDataSource"))
        )
    }
}

val commonRecipeModule = module {
    single { DatabaseFactory(databaseDriverFactory = get()).createDatabase() }
    single { FoodServiceApi() }
    single<RecipeDataSource>(named("recipeLocalDataSource")) { RecipeLocalDataSource(get()) }
    single<RecipeDataSource>(named("recipeRemoteDataSource")) { RecipeRemoteDataSource(get()) }
    single<RecipeRepository> {
        RecipeDataRepository(
            recipeLocalDataSource = get(named("recipeLocalDataSource")),
            recipeRemoteDataSource = get(named("recipeRemoteDataSource"))
        )
    }
}

