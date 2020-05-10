package com.example.savethefood.recipe

import android.app.Application
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.constants.ApiCallStatus
import com.example.savethefood.constants.Done
import com.example.savethefood.constants.Loading
import com.example.savethefood.constants.Error
import com.example.savethefood.data.Result
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.repository.RecipeDataRepository
import com.example.savethefood.data.source.repository.RecipeRepository
import kotlinx.coroutines.*

class RecipeViewModel(
    private val recipeRepository: RecipeRepository,
    foodName: String?
) : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ApiCallStatus>(Done("Done"))

    // The external immutable LiveData for the request status
    val status: LiveData<ApiCallStatus>
        get() = _status

    private var _recipeList = MediatorLiveData<RecipeDomain>()
    val recipeList: LiveData<RecipeDomain>
        get() = _recipeList

    private var _recipeListResult = MediatorLiveData<List<RecipeResult?>>()
    val recipeListResult: LiveData<List<RecipeResult?>>
        get() = _recipeListResult

    private val _recipeDetailEvent = MutableLiveData<Event<RecipeResult>>()
    val recipeDetailEvent: LiveData<Event<RecipeResult>>
        get() = _recipeDetailEvent

    init {
        getRecipes(foodName)
    }

    private fun getRecipes(food: String?) {
        viewModelScope.launch {
            try {
                _status.value = Loading("Loading")
                val recipes = recipeRepository.getRecipes(food) ?: return@launch
                if (recipes is Result.Success) {
                    _recipeList.value = recipes.data
                    _recipeListResult.value = recipes.data.results
                } else {
                    throw Exception(recipes.toString())
                }
                _status.value = Done("Done")
            } catch (e: Exception) {
                _status.value = Error(e.message.let { toString() })
                _recipeList.value = null
            }
        }
    }

    fun moveToRecipeDetail(recipe: RecipeResult) {
        _recipeDetailEvent.value = Event(recipe)
    }

    fun updateDataList(list: ArrayList<RecipeResult?>) {
        _recipeListResult.value = list
    }

    class RecipeViewModelFactory(
        private val dataRepository: RecipeRepository,
        private val foodName: String?
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RecipeViewModel(
                    dataRepository,
                    foodName
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}