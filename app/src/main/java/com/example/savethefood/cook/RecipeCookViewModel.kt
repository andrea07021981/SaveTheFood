package com.example.savethefood.cook

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.shared.data.domain.RecipeInfoDomain


class RecipeCookViewModel @ViewModelInject constructor(
    @Assisted private val recipe: SavedStateHandle
) : ViewModel(){

    private var _recipeInfoDomain = MutableLiveData<RecipeInfoDomain?>(
        recipe.get("recipeInfoDomain")
    )
    val recipeInfoDomain: LiveData<RecipeInfoDomain?>
        get() = _recipeInfoDomain
}