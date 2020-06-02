package com.example.savethefood.food

import androidx.lifecycle.*
import com.example.savethefood.constants.ApiCallStatus
import com.example.savethefood.constants.Done
import com.example.savethefood.constants.Error
import com.example.savethefood.constants.Loading
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

    private val _status = MutableLiveData<ApiCallStatus>(Done("Done"))
    val status: LiveData<ApiCallStatus>
        get() = _status

    public val foodName = MutableLiveData<String>()

    init {
        foodName.value = "test";
    }

    fun searchFood() {
        viewModelScope.launch {
            try {
                _status.value = Loading("Loading")
                //TODO search food list by name https://spoonacular.com/food-api/docs#Search-Grocery-Products
                val foodResult = dataRepository.getApiFoodQuery(foodName.value!!)
                if (foodResult is Result.Success) {
                    //_foodListResult.postValue(foodResult.data.products)
                } else {

                }
                _status.value = Done("Done")
            } catch (e: Exception) {
                _status.value = Error(toString())
            }
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