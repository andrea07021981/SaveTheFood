package com.example.savethefood.recipe

import androidx.annotation.VisibleForTesting
import androidx.arch.core.util.Function
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.constants.*
import com.example.savethefood.constants.ApiCallStatus.*
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.repository.RecipeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Error


class RecipeViewModel(
    val recipeRepository: RecipeRepository,
    foodName: String?
) : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ApiCallStatus>(Done("Done"))

    // The external immutable LiveData for the request status
    val status: LiveData<ApiCallStatus>
        get() = _status

    //livedata filter, every time it changes and emit signal the switch map is activated and filter the private list
    var searchFilter = MutableLiveData<String>("")
        @VisibleForTesting set
    private val _searchFilter: MutableLiveData<String>
        get() = searchFilter

    var _recipeListResult: LiveData<List<RecipeResult>?> =
        recipeRepository.getRecipes(foodName)
            .onStart {
                _status.value = Loading("Loading")
            }
            .catch {
                _status.value = ApiCallStatus.Error("Error Loading") //TODO create a class with Errors
            }
            .transform { value ->
                if (value is Result.Success) {
                    emit(value.data.results)
                }
            }
            .onCompletion {
                _status.value = Done("Done")
            }
            .asLiveData(viewModelScope.coroutineContext)
        @VisibleForTesting set // this allow us to use this set only for test

    val recipeListResult = Transformations.switchMap(_searchFilter) { // OR    _searchFilter.switchMap {
        if (it != null && it.isNotEmpty() ) {
            return@switchMap _recipeListResult.map { list ->
                list?.filter { recipe ->
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