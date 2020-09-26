package com.example.savethefood.cook

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.savethefood.data.domain.RecipeInfoDomain

class RecipeCookViewModel(
    recipe: RecipeInfoDomain
) : ViewModel(){

    private var _recipeInfoDomain = MutableLiveData<RecipeInfoDomain>(recipe)
    val recipeInfoDomain: LiveData<RecipeInfoDomain>
        get() = _recipeInfoDomain

    class Factory(
        private val recipe: RecipeInfoDomain
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecipeCookViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RecipeCookViewModel(
                    recipe = recipe
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}