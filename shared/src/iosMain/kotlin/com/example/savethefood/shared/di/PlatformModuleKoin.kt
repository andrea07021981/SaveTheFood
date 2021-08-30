package com.example.savethefood.shared.di

import com.example.savethefood.shared.data.source.local.database.DatabaseDriverFactory
import com.example.savethefood.shared.viewmodel.LoginViewModel
import org.koin.core.module.Module
import org.koin.dsl.module


actual val platformModule = module {
    module {
        single { DatabaseDriverFactory() }
        //single { LoginViewModel(userDataRepository = get()) }
    }
}