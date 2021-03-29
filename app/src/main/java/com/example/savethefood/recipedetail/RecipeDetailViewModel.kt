package com.example.savethefood.recipedetail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.constants.ApiCallStatus
import com.example.savethefood.constants.ApiCallStatus.*
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.repository.RecipeRepository
import kotlinx.coroutines.launch

class RecipeDetailViewModel @ViewModelInject constructor(
    private val recipeRepository: RecipeRepository,
    @Assisted recipeResult: SavedStateHandle
) : ViewModel() {

    //TODO ADD CHECK STATUS OF SAVED RECIPE AND BIND IT

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ApiCallStatus>(Done("Done"))

    // The external immutable LiveData for the request status
    val status: LiveData<ApiCallStatus>
        get() = _status

    private var _recipeDetail = MutableLiveData<RecipeInfoDomain?>()
    val recipeDetail: LiveData<RecipeInfoDomain?>
        get() = _recipeDetail

    private val _recipeListEvent = MutableLiveData<Event<Unit>>()
    val recipeListEvent: LiveData<Event<Unit>>
        get() = _recipeListEvent

    private val _recipeCookingEvent = MutableLiveData<Event<RecipeInfoDomain>>()
    val recipeCookingEvent: LiveData<Event<RecipeInfoDomain>>
        get() = _recipeCookingEvent

    init {
        getRecipeDetails(recipeResult.get<RecipeResult>("recipeResult") ?: RecipeResult())
    }

    private fun getRecipeDetails(recipeResult: RecipeResult) {
        // TODO move to livedata constructor, remove status and use the result that we will recive from repository to show the status
        viewModelScope.launch {
            try {
                _status.value = Loading("Loading")
                val recipe = recipeRepository.getRecipeInfo(recipeResult.id)
                if (recipe is Result.Success) {
                    _recipeDetail.value = recipe.data
                } else {
                    throw Exception(recipe.toString())
                }
                _status.value = Done("Done")
            } catch (e: Exception) {
                _status.value = ApiCallStatus.Error(toString())
                _recipeDetail.value = null
            }
        }
    }

    fun backToRecipeList() {
        _recipeListEvent.value = Event(Unit)
    }

    fun moveToCookDetail(recipe: RecipeInfoDomain) {
        _recipeCookingEvent.value = Event(recipe)
    }

    fun saveRecipe(recipe: RecipeInfoDomain) {
        //TODO SAVE IN LOCAL THE RECIPE, ADD A LIVE DATA AND DATABINDIG FOR HEART ICON
        viewModelScope.launch {
            recipeRepository.saveRecipe(recipe)
        }
    }
}