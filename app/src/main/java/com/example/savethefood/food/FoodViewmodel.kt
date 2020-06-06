package com.example.savethefood.food

import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.constants.ApiCallStatus
import com.example.savethefood.constants.Done
import com.example.savethefood.constants.Error
import com.example.savethefood.constants.Loading
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.FoodSearchDomain
import com.example.savethefood.data.domain.ProductDomain
import com.example.savethefood.data.source.repository.FoodDataRepository
import kotlinx.coroutines.launch

class FoodViewmodel(
    private val dataRepository: FoodDataRepository
) : ViewModel() {

    private var _foodListResult = MutableLiveData<List<ProductDomain>?>()
    val foodListResult: LiveData<List<ProductDomain>?>
        get() = _foodListResult

    private var _foodDomain = MutableLiveData<Result<FoodDomain>>()
    val foodDomain: LiveData<Result<FoodDomain>>
        get() = _foodDomain

    private val _search = MutableLiveData<Event<Unit>>()
    val search: LiveData<Event<Unit>>
        get() = _search

    private val _status = MutableLiveData<ApiCallStatus>(Done("Done"))
    val status: LiveData<ApiCallStatus>
        get() = _status

    val foodName = MutableLiveData<String>()

    init {
        foodName.value = "";
    }

    fun saveFood(food: ProductDomain) {
        viewModelScope.launch {
            //Call the apy with foodcode
            //dataRepository.saveNewFood(food)
            //TODO check whether the record has been inserted or not
            //_goHomeEvent.value = Event(Unit)
        }
    }

    fun searchFood() {
        _search.value = Event(Unit)
        viewModelScope.launch {
            try {
                _status.value = Loading("Loading")
                val foodResult = dataRepository.getApiFoodQuery(foodName.value!!)
                if (foodResult is Result.Success) {
                    _foodListResult.postValue(foodResult.data.products)
                } else {
                    throw Exception(foodResult.toString())
                }
                _status.value = Done("Done")
            } catch (e: Exception) {
                _status.value = Error(toString())
            }
        }
    }

    fun saveFoodDetail(food: ProductDomain) {
        viewModelScope.launch {
            try {
                _status.value = Loading("Loading")
                val foodResult = dataRepository.getApiFoodById(food.id)
                if (foodResult is Result.Success) {
                    _foodDomain.value = foodResult
                } else {
                    throw Exception(foodResult.toString())
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