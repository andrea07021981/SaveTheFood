package com.example.savethefood.shared.di

import com.example.savethefood.shared.data.source.local.database.DatabaseDriverFactory
import com.example.savethefood.shared.viewmodel.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.scope.get
import org.koin.dsl.module

actual val platformModule = module {
    single { DatabaseDriverFactory(androidContext()) }
    viewModel { AddFoodViewModel(foodDataRepository = get()) }
    viewModel { FoodDetailViewModel(foodDataRepository = get(), recipeDataRepository = get(), state = get()) }
    viewModel { HomeViewModel(foodDataRepository = get()) }
    viewModel { LoginViewModel(userDataRepository = get()) }
    viewModel { MapViewModel() }
    viewModel { PlanViewModel() }
    viewModel { RecipeCookViewModel() }
    viewModel { RecipeDetailViewModel(recipeDataRepository = get(), state = get()) }
    viewModel { RecipeViewModel(recipeDataRepository = get()) }
}