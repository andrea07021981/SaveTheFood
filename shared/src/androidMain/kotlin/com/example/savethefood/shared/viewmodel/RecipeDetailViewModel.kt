package com.example.savethefood.shared.viewmodel

import androidx.lifecycle.*
import com.example.savethefood.shared.data.domain.RecipeInfoDomain
import com.example.savethefood.shared.data.domain.RecipeResult
import com.example.savethefood.shared.data.source.repository.FoodRepository
import com.example.savethefood.shared.data.source.repository.RecipeRepository
import com.example.savethefood.shared.utils.ApiCallStatus
import com.example.savethefood.shared.utils.ApiCallStatus.*
import com.example.savethefood.shared.utils.Event
import kotlinx.coroutines.launch

actual class RecipeDetailViewModel actual constructor(
    private val recipeDataRepository: RecipeRepository
) : ViewModel() {

    constructor(
        recipeDataRepository: RecipeRepository,
        state: SavedStateHandle
    ) : this(recipeDataRepository) {
        with(state.get<RecipeResult>("recipeResult") ?: RecipeResult()) {
            getRecipeDetails(this)
            _recipeResult.value = this
        }
    }
    //TODO ADD CHECK STATUS OF SAVED RECIPE AND BIND IT

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ApiCallStatus>(Done("Done"))

    // The external immutable LiveData for the request status
    val status: LiveData<ApiCallStatus>
        get() = _status

    private var _recipeResult = MutableLiveData<RecipeResult?>()
    val recipeResult: LiveData<RecipeResult?>
        get() = _recipeResult

    private var _recipeDetail = MutableLiveData<RecipeInfoDomain?>()
    val recipeDetail: LiveData<RecipeInfoDomain?>
        get() = _recipeDetail

    private val _recipeListEvent = MutableLiveData<Event<Unit>>()
    val recipeListEvent: LiveData<Event<Unit>>
        get() = _recipeListEvent

    private val _recipeCookingEvent = MutableLiveData<Event<RecipeInfoDomain>>()
    val recipeCookingEvent: LiveData<Event<RecipeInfoDomain>>
        get() = _recipeCookingEvent

    private fun getRecipeDetails(recipeResult: RecipeResult) {
        // TODO move to livedata constructor, remove status and use the result that we will recive from repository to show the status
        viewModelScope.launch {
            try {
                _status.value = Loading("Loading")
                val recipe = recipeDataRepository.getRecipeInfo(recipeResult.id.toInt())
                if (recipe is com.example.savethefood.shared.data.Result.Success) {
                    _recipeDetail.value = recipe.data
                } else {
                    throw Exception(recipe.toString())
                }
                _status.value = Done("Done")
            } catch (e: Exception) {
                _status.value = Error(toString())
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
            //recipeRepository.saveRecipe(recipe)
        }
    }
}