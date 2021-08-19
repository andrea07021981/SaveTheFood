package com.example.savethefood.shared.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import com.example.savethefood.shared.constant.Constants.BUNDLE_FOOD_KEY
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.data.domain.RecipeIngredients
import com.example.savethefood.shared.data.source.repository.FoodRepository
import com.example.savethefood.shared.data.source.repository.RecipeRepository
import com.example.savethefood.shared.utils.Event
import com.example.savethefood.shared.utils.launchDataLoad
import com.example.savethefood.shared.utils.retrieveFood
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

actual class FoodDetailViewModel actual constructor(
    private val foodDataRepository: com.example.savethefood.shared.data.source.repository.FoodRepository,
    private val recipeDataRepository: RecipeRepository,
) : ViewModel() {

    private lateinit var currentState: SavedStateHandle

    constructor(
        foodDataRepository: FoodRepository,
        recipeDataRepository: RecipeRepository,
        state: SavedStateHandle
        ) : this(foodDataRepository, recipeDataRepository) {
            currentState = state
        }

    private val _recipeAdded = MutableLiveData<com.example.savethefood.shared.data.Result<RecipeIngredients?>>()
    val recipeAdded: LiveData<com.example.savethefood.shared.data.Result<RecipeIngredients?>>
        get() = _recipeAdded

    private val _food = MutableLiveData<FoodDomain>()
    val food: LiveData<FoodDomain>
        get() = _food

    private val _deleteFoodEvent = MutableLiveData<Event<Boolean>>()
    val deleteFoodEvent: LiveData<Event<Boolean>>
        get() = _deleteFoodEvent

    private val _recipeFoodEvent = MutableLiveData<Event<FoodDomain>>()
    val recipeFoodEvent: LiveData<Event<FoodDomain>>
        get() = _recipeFoodEvent

    private val _editFoodEvent = MutableLiveData<Event<FoodDomain>>()
    val editFoodEvent: LiveData<Event<FoodDomain>>
        get() = _editFoodEvent

    private val _errorData = MutableLiveData<Event<String>>()
    val errorData: LiveData<Event<String>>
        get() = _errorData

    // Collect the list without and filter the current food
    // TODO can we move all methods in repositories but either keep the transform or handle
    // TODO it with databinding, remove errordata and use the result live recipes
    private val _foodList: LiveData<List<com.example.savethefood.shared.data.domain.FoodDomain>?> = foodDataRepository.getFoods()
        .onStart {
            emit(com.example.savethefood.shared.data.Result.Loading)
        }
        .catch { error ->
            emit(com.example.savethefood.shared.data.Result.ExError(Exception(error.message)))
        }
        .transform { value ->
            when (value) {
                is com.example.savethefood.shared.data.Result.Success -> emit(value.data.filter { it.id != _food.value?.id }) // Remove the current food
                is com.example.savethefood.shared.data.Result.ExError -> _errorData.value = Event(value.exception.localizedMessage)
                is com.example.savethefood.shared.data.Result.Error -> _errorData.value = Event(value.message)
                else -> Unit
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    val foodList: LiveData<List<FoodDomain>?> = _foodList

    private val foodsFilterList: ArrayList<String> = arrayListOf()
    private var _foodFilter = MutableLiveData(foodsFilterList)
    private val _recipeList: LiveData<com.example.savethefood.shared.data.Result<List<RecipeIngredients>?>> = _foodFilter.switchMap {
        recipeDataRepository.getRecipesByIngredients(*it.toTypedArray())
            .onStart {
                emit(com.example.savethefood.shared.data.Result.Loading)
            }
            .catch { error ->
                emit(com.example.savethefood.shared.data.Result.ExError(Exception(error.message)))
            }
            .transform { value ->
                emit(value)
            }
            .onCompletion {
                // In this case it's called because the channel is closed in datasource
                //_status.value = ApiCallStatus.Done()
            }
            .asLiveData(viewModelScope.coroutineContext)
    }

    val recipeList: LiveData<com.example.savethefood.shared.data.Result<List<RecipeIngredients>?>> = _recipeList

    init {
        _food.value = currentState.get<Bundle>(BUNDLE_FOOD_KEY).retrieveFood()
        foodsFilterList.add(_food.value?.title ?: "")
    }

    fun deleteFood() {
        viewModelScope.launch {
            try {
                _deleteFoodEvent.value = _food.value?.let {
                    Event(foodDataRepository.deleteFood(it) != 0L)
                } ?: Event(false)
            } catch (e: NullPointerException) {
                Log.e("FoodDetail", e.message ?: "No Message")
                Event(false)
            }
        }
    }

    fun navigateToRecipeSearch(recipe: FoodDomain) {
        _recipeFoodEvent.value = Event(recipe)
    }

    fun navigateToFoodEdit() {
        _food.value?.let {
            _editFoodEvent.value = Event(it)
        }
    }

    fun updateRecipeList(filter: String) {
        with(foodsFilterList) {
            if (contains(filter)) {
                remove(filter)
            } else {
                add(filter)
            }
        }
        _foodFilter.value = foodsFilterList
    }

    fun saveRecipe(recipe: RecipeIngredients) {
        launchDataLoad(_recipeAdded) {
            recipeDataRepository.saveRecipe(recipe)
        }
    }
}