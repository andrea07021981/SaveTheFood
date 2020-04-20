package com.example.savethefood.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.savethefood.constants.ApiCallStatus
import com.example.savethefood.constants.Done
import com.example.savethefood.constants.Loading
import com.example.savethefood.constants.Error
import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.local.domain.RecipeDomain
import com.example.savethefood.local.domain.RecipeResult
import com.example.savethefood.repository.RecipeRepository
import kotlinx.coroutines.*

class RecipeViewModel(
    application: Application,
    foodId: String?
) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = SaveTheFoodDatabase.getInstance(application)
    private val recipesRepository = RecipeRepository(database)

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

    private var _navigateToRecipeDetail = MediatorLiveData<RecipeResult>()
    val navigateToRecipeDetail: LiveData<RecipeResult>
        get() = _navigateToRecipeDetail

    init {
        getRecipes(foodId)
    }

    private fun getRecipes(food: String?) {
        viewModelScope.launch {
            try {
                _status.value = Loading("Loading")
                val recipes = recipesRepository.getRecipes(food)
                _recipeList.value = recipes
                _recipeListResult.value = recipes.results
                _status.value = Done("Done")
            } catch (e: Exception) {
                _status.value = Error(e.message.let { toString() })
                _recipeList.value = null
            }
        }
    }

    fun moveToRecipeDetail(recipe: RecipeResult) {
        _navigateToRecipeDetail.value = recipe
    }

    fun doneToRecipeDetail() {
        _navigateToRecipeDetail.value = null
    }

    public fun updateDataList(list: ArrayList<RecipeResult?>) {
        _recipeListResult.value = list
    }

    class Factory(val app: Application, val foodId: String?) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RecipeViewModel(app, foodId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}