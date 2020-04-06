package com.example.savethefood.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.savethefood.R
import com.example.savethefood.constants.ApiCallStatus
import com.example.savethefood.constants.Done
import com.example.savethefood.constants.Error
import com.example.savethefood.constants.Loading
import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.local.domain.RecipeInfoDomain
import com.example.savethefood.local.domain.RecipeResult
import com.example.savethefood.repository.RecipeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.ResponseCache

class RecipeDetailViewModel(
    application: Application,
    recipeResult: RecipeResult
) : ViewModel() {


    //TODO ADD CHECK STATUS OF SAVED RECIPE AND BIND IT

    private val viewModelJob = Job()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = SaveTheFoodDatabase.getInstance(application)
    private val recipesRepository = RecipeRepository(database)

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ApiCallStatus>(Done("Done"))

    // The external immutable LiveData for the request status
    val status: LiveData<ApiCallStatus>
        get() = _status

    private var _recipeDetail = MutableLiveData<RecipeInfoDomain>()
    val recipeDetail: LiveData<RecipeInfoDomain>
        get() = _recipeDetail

    private var _navigateToRecipeList = MediatorLiveData<Boolean>()
    val navigateToRecipeList: LiveData<Boolean>
        get() = _navigateToRecipeList

    private var _navigateToRecipeCooking = MediatorLiveData<RecipeInfoDomain>()
    val navigateToRecipeCooking: LiveData<RecipeInfoDomain>
        get() = _navigateToRecipeCooking

    init {
        getRecipeDetails(recipeResult)
    }

    private fun getRecipeDetails(recipeResult: RecipeResult) {
        viewModelScope.launch {
            try {
                _status.value = Loading("Loading")
                val recipe = recipesRepository.getRecipeInfo(recipeResult.id)
                _recipeDetail.value = recipe
                _status.value = Done("Done")
            } catch (e: Exception) {
                _status.value = Error(toString())
                _recipeDetail.value = null
            }
        }
    }

    fun backToRecipeList() {
        _navigateToRecipeList.value = true
    }

    fun doneBackToRecipeList() {
        _navigateToRecipeList.value = null
    }

    fun moveToCookDetail(recipe: RecipeInfoDomain) {
        _navigateToRecipeCooking.value = recipe
    }

    fun doneToCookDetail() {
        _navigateToRecipeCooking.value = null
    }

    fun saveRecipe(recipe: RecipeInfoDomain) {
        //TODO SAVE IN LOCAL THE RECIPE, ADD A LIVE DATA AND DATABINDIG FOR HEART ICON
    }

    class Factory(val application: Application, val recipeResult: RecipeResult) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RecipeDetailViewModel(application = application, recipeResult = recipeResult) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}