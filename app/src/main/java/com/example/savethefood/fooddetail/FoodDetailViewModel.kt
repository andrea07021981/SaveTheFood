package com.example.savethefood.fooddetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.repository.FoodDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.lang.NullPointerException

class FoodDetailViewModel(
    app: Application,
    food: FoodDomain
) : ViewModel() {

    private val viewmodelJob = Job()
    private val viewmodelScope = CoroutineScope(viewmodelJob + Dispatchers.IO)
    private val database = SaveTheFoodDatabase.getInstance(app)
    private val foodsRepository = FoodDataRepository(database)

    private val _food = MutableLiveData<FoodDomain>()
    val food: LiveData<FoodDomain>
        get() = _food

    private val _recipeFoodEvent = MutableLiveData<Event<FoodDomain>>()
    val recipeFoodEvent: LiveData<Event<FoodDomain>>
        get() = _recipeFoodEvent

    init {
        _food.value = food
    }

    fun deleteFood() {
        viewmodelScope.launch {
            try {
                foodsRepository.deleteFood(_food.value)
            } catch (e: NullPointerException) {
                Log.e("FoodDetail", e.message)
            }
        }
    }

    fun moveToRecipeSearch(recipe: FoodDomain) {
        _recipeFoodEvent.value = Event(recipe)
    }

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val application: Application, val foodSelected: FoodDomain) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FoodDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FoodDetailViewModel(
                    app = application,
                    food = foodSelected
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}