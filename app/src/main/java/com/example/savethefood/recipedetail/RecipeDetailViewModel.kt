package com.example.savethefood.recipedetail

import android.app.Application
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.constants.ApiCallStatus
import com.example.savethefood.constants.ApiCallStatus.*
import com.example.savethefood.data.Result
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.repository.RecipeDataRepository
import com.example.savethefood.data.source.repository.RecipeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.Error

class RecipeDetailViewModel(
    private val recipeRepository: RecipeRepository,
    recipeResult: RecipeResult
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

    private val _recipeCookingtEvent = MutableLiveData<Event<RecipeInfoDomain>>()
    val recipeCookingtEvent: LiveData<Event<RecipeInfoDomain>>
        get() = _recipeCookingtEvent

    init {
        getRecipeDetails(recipeResult)
    }

    private fun getRecipeDetails(recipeResult: RecipeResult) {
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
        _recipeCookingtEvent.value = Event(recipe)
    }

    fun saveRecipe(recipe: RecipeInfoDomain) {
        //TODO SAVE IN LOCAL THE RECIPE, ADD A LIVE DATA AND DATABINDIG FOR HEART ICON
        viewModelScope.launch {
            recipeRepository.saveRecipe(recipe)
        }
    }

    class RecipeDetailViewModelFactory(
        private val dataRepository: RecipeRepository,
        private val recipeResult: RecipeResult
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RecipeDetailViewModel(
                    dataRepository,
                    recipeResult
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}