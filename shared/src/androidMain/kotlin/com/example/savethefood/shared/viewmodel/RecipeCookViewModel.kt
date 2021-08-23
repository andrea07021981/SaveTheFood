package com.example.savethefood.shared.viewmodel

import androidx.lifecycle.*
import com.example.savethefood.shared.data.domain.RecipeInfoDomain
import com.example.savethefood.shared.data.source.repository.FoodRepository
import com.example.savethefood.shared.data.source.repository.RecipeRepository


actual class RecipeCookViewModel actual constructor(

) : ViewModel(){

    constructor(
        state: SavedStateHandle
    ) : this() {
        _recipeInfoDomain.value = state.get("recipeInfoDomain")
    }

    private var _recipeInfoDomain = MutableLiveData<RecipeInfoDomain?>()
    val recipeInfoDomain: LiveData<RecipeInfoDomain?>
        get() = _recipeInfoDomain
}