package com.example.savethefood.fooddetail

import android.app.Application
import android.text.Html
import android.util.Log
import androidx.core.text.HtmlCompat
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.data.Result
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.repository.FoodDataRepository
import com.example.savethefood.data.source.repository.FoodRepository
import com.example.savethefood.data.source.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import javax.inject.Inject

class FoodDetailViewModel @ViewModelInject constructor(
    private val foodDataRepository: FoodRepository,
    @Assisted food: SavedStateHandle
) : ViewModel() {

    private val _food = MutableLiveData<FoodDomain>()
    val food: LiveData<FoodDomain>
        get() = _food

    private val _foodDeleted = MutableLiveData<Event<Boolean>>()
    val foodDeleted: LiveData<Event<Boolean>>
        get() = _foodDeleted

    private val _recipeFoodEvent = MutableLiveData<Event<FoodDomain>>()
    val recipeFoodEvent: LiveData<Event<FoodDomain>>
        get() = _recipeFoodEvent

    private val _errorData = MutableLiveData<Event<String>>()
    val errorData: LiveData<Event<String>>
        get() = _errorData

    private val _foodList: LiveData<List<FoodDomain>?> = foodDataRepository.getFoods()
        .transform { value ->
            when (value) {
                is Result.Success -> emit(value.data)
                is Result.ExError -> _errorData.value = Event(value.exception.localizedMessage)
                else -> Unit
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    val foodList: LiveData<List<FoodDomain>?> = _foodList

    init {
        _food.value = food.get<FoodDomain>("foodDomain") ?: FoodDomain()
    }

    fun deleteFood() {
        viewModelScope.launch {
            try {
                val idDeleted = foodDataRepository.deleteFood(_food.value)
                _foodDeleted.value = Event(idDeleted != 0)
            } catch (e: NullPointerException) {
                Log.e("FoodDetail", e.message)
            }
        }
    }

    fun moveToRecipeSearch(recipe: FoodDomain) {
        _recipeFoodEvent.value = Event(recipe)
    }
/*
    *//**
     * Factory for constructing DevByteViewModel with parameter
     *//*
    class FoodDetailViewModelFactory(
        private val dataRepository: FoodRepository,
        private val foodSelected: FoodDomain
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FoodDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FoodDetailViewModel(
                    foodDataRepository = dataRepository,
                    food = foodSelected
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }*/
}