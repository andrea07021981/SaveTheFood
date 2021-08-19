package com.example.savethefood.shared.di

import com.example.savethefood.shared.data.source.local.database.DatabaseDriverFactory
import com.example.savethefood.shared.viewmodel.FoodDetailViewModel
import com.example.savethefood.shared.viewmodel.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule = module {
    single { DatabaseDriverFactory(androidContext()) }
    viewModel { LoginViewModel(userDataRepository = get()) }
    viewModel { FoodDetailViewModel(foodDataRepository = get(), recipeDataRepository = get(), state = get()) }
}