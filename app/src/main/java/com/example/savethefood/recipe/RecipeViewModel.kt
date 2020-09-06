package com.example.savethefood.recipe

import androidx.arch.core.util.Function
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.constants.ApiCallStatus
import com.example.savethefood.constants.Done
import com.example.savethefood.constants.Error
import com.example.savethefood.constants.Loading
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.repository.RecipeRepository
import kotlinx.coroutines.launch


class RecipeViewModel(
    private val recipeRepository: RecipeRepository,
    foodName: String?
) : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ApiCallStatus>(Done("Done"))

    // The external immutable LiveData for the request status
    val status: LiveData<ApiCallStatus>
        get() = _status

    //livedata filter, every time it changes and emit signal the switch map is activated and filter the private list
    private var _searchFilter = MutableLiveData<String>("")
    private var _recipeListResult = MutableLiveData<List<RecipeResult>>()
    val recipeListResult: LiveData<List<RecipeResult>> = Transformations.switchMap(_searchFilter) { // OR    _searchFilter.switchMap {
        if (it.isNotEmpty()) {
            return@switchMap _recipeListResult.map { list ->
                list.filter { recipe ->
                    recipe.title.toLowerCase().contains(it.toLowerCase())
                }
            }
        } else {
            return@switchMap _recipeListResult
        }
    }

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
                    _recipeListResult.value = recipes.data.results
                } else {
                    throw Exception(recipes.toString())
                }
                _status.value = Done("Done")
            } catch (e: Exception) {
                _status.value = Error(e.message.let { toString() })
            }
        }
    }

    fun moveToRecipeDetail(recipe: RecipeResult) {
        _recipeDetailEvent.value = Event(recipe)
    }

    fun updateDataList(filter: String) {
       _searchFilter.value = filter
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