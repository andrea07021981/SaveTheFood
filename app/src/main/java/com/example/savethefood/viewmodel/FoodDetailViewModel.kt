package com.example.savethefood.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.savethefood.local.domain.FoodDomain
import java.lang.IllegalArgumentException

class FoodDetailViewModel(
    app: Application,
    food: FoodDomain
) : ViewModel() {

    private val _food = MutableLiveData<FoodDomain>()
    val food: LiveData<FoodDomain>
        get() = _food

    init {
        _food.value = food
    }

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val application: Application, val foodSelected: FoodDomain) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FoodDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FoodDetailViewModel(app = application, food = foodSelected) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}