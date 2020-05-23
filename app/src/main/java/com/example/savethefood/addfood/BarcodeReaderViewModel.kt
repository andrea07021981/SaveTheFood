package com.example.savethefood.addfood

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.data.Result
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.repository.FoodDataRepository
import com.example.savethefood.data.source.repository.FoodRepository
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.*
import java.lang.Exception

class BarcodeReaderViewModel(
    private val foodDataRepository: FoodRepository
) : ViewModel() {

    private val _food = MutableLiveData<FoodDomain>()
    val food: LiveData<FoodDomain>
        get() = _food
    private val _barcodeResult = MutableLiveData<Result<FoodDomain>>()
    val barcodeResult: LiveData<Result<FoodDomain>>
        get() = _barcodeResult
    private val _readBarcodeEvent = MutableLiveData<Event<Unit>>()
    val readBarcodeEvent: LiveData<Event<Unit>>
        get() = _readBarcodeEvent
    private val _goHomeEvent = MutableLiveData<Event<Unit>>()
    val goHomeEvent: LiveData<Event<Unit>>
        get() = _goHomeEvent
    private var _progressVisibility = MediatorLiveData<Boolean>()
    val progressVisibility: LiveData<Boolean>
        get() = _progressVisibility
    /*
    If food id has a value of 0 it means that we don't have a valid response from api.
     */
    val validBarcode = Transformations.map(_food) {
        0 != it.foodId
    }

    init {
        _food.value =
            FoodDomain()
    }

    fun searchInfoByBarcode() {
        _readBarcodeEvent.value = Event(Unit)
    }

    fun searchOnline() {
        //Todo use spoonacular to find a fit for the food. Show a list and let the user select
    }

    fun getApiFoodDetails(barcode: String) {
        viewModelScope.launch {
            try {
                _progressVisibility.value = true
                val foodRetrieved = foodDataRepository.getApiFoodUpc(barcode)
                if (foodRetrieved is Result.Success) {
                    _food.postValue(foodRetrieved.data)
                } else {
                    _barcodeResult.postValue(foodRetrieved)
                }
                Log.d("Food title", _food.value?.foodTitle)
                Log.d("Food description", _food.value?.foodDescription)
            } catch (error: JsonDataException) {
                Log.d("Error retrofit json ", error.message)
            } catch (generic: Exception) {
                Log.d("Generic exception ", generic.message)
            } finally {
                _progressVisibility.value = false
            }
        }
    }

    fun saveFood(food: FoodDomain) {
         viewModelScope.launch {
             foodDataRepository.saveNewFood(food)
             //TODO check whether the record has been inserted or not
             _goHomeEvent.value = Event(Unit)
         }
    }

    /*
     * Factory for constructing DevByteViewModel with parameter
     */
    class BarcodeViewModelFactory(private val dataRepository: FoodRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BarcodeReaderViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BarcodeReaderViewModel(dataRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}