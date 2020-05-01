package com.example.savethefood.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.savethefood.data.domain.RecipeInfoDomain

class RecipeCookViewModel(
    application: Application,
    recipe: RecipeInfoDomain
) : ViewModel(){

    private var _recipeInfoDomain = MutableLiveData<RecipeInfoDomain>()
    val recipeInfoDomain: LiveData<RecipeInfoDomain>
        get() = _recipeInfoDomain

    init {
        _recipeInfoDomain.value = recipe
    }

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