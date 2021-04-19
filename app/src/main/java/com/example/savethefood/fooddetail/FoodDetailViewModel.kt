package com.example.savethefood.fooddetail

import android.os.Bundle
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.constants.Constants.BUNDLE_FOOD_KEY
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.RecipeIngredients
import com.example.savethefood.data.source.repository.FoodRepository
import com.example.savethefood.data.source.repository.RecipeRepository
import com.example.savethefood.util.launchDataLoad
import com.example.savethefood.util.retrieveFood
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import java.lang.Exception

class FoodDetailViewModel @ViewModelInject constructor(
    private val foodDataRepository: FoodRepository,
    private val recipeDataRepository: RecipeRepository,
    @Assisted food: SavedStateHandle
) : ViewModel() {

    private val _recipeAdded = MutableLiveData<Result<RecipeIngredients?>>()
    val recipeAdded: LiveData<Result<RecipeIngredients?>>
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
    private val _foodList: LiveData<List<FoodDomain>?> = foodDataRepository.getFoods()
        .onStart {
            emit(Result.Loading)
        }
        .catch { error ->
            emit(Result.ExError(Exception(error.message)))
        }
        .transform { value ->
            when (value) {
                is Result.Success -> emit(value.data.filter { it.id != _food.value?.id }) // Remove the current food
                is Result.ExError -> _errorData.value = Event(value.exception.localizedMessage)
                is Result.Error -> _errorData.value = Event(value.message)
                else -> Unit
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    val foodList: LiveData<List<FoodDomain>?> = _foodList

    private val foodsFilterList: ArrayList<String> = arrayListOf()
    private var _foodFilter = MutableLiveData(foodsFilterList)
    private val _recipeList: LiveData<Result<List<RecipeIngredients>?>> = _foodFilter.switchMap {
        recipeDataRepository.getRecipesByIngredients(*it.toTypedArray())
            .onStart {
                emit(Result.Loading)
            }
            .catch { error ->
                emit(Result.ExError(Exception(error.message)))
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

    val recipeList: LiveData<Result<List<RecipeIngredients>?>> = _recipeList

    init {
        _food.value = food.get<Bundle>(BUNDLE_FOOD_KEY).retrieveFood()
        foodsFilterList.add(_food.value?.title ?: "")
    }

    fun deleteFood() {
        viewModelScope.launch {
            try {
                _deleteFoodEvent.value = _food.value?.let {
                    Event(foodDataRepository.deleteFood(it) != 0)
                } ?: Event(false)
            } catch (e: NullPointerException) {
                Log.e("FoodDetail", e.message)
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