package com.example.savethefood.food

import androidx.lifecycle.*
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.repository.FoodDataRepository
import kotlinx.coroutines.launch

class FoodViewmodel(
    private val dataRepository: FoodDataRepository
) : ViewModel() {

    private var _foodListResult = MutableLiveData<Result<List<FoodDomain>>?>()
    val foodListResult: LiveData<Result<List<FoodDomain>>?>
        get() = _foodListResult

    fun searchFood() {
        viewModelScope.launch {

        }
    }

    class FoodViewModelFactory(
        private val dataRepository: FoodDataRepository
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FoodViewmodel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FoodViewmodel(dataRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}