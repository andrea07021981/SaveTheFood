package com.example.savethefood.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.savethefood.local.domain.RecipeInfoDomain

class RecipeCookViewModel(
    application: Application,
    recipe: RecipeInfoDomain
) : ViewModel(){

    class Factory(
        private val application: Application,
        private val recipe: RecipeInfoDomain
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecipeCookViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RecipeCookViewModel(application = application, recipe = recipe) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}