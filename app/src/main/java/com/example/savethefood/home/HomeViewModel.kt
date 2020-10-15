package com.example.savethefood.home

import android.app.Application
import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.R
import com.example.savethefood.data.Result
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.repository.FoodDataRepository
import com.example.savethefood.data.source.repository.FoodRepository
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.*
import java.lang.Exception

class HomeViewModel @ViewModelInject constructor(
    private val foodDataRepository: FoodRepository
) : ViewModel() {

    val animationResourceButton = R.anim.fade_in

    private var _foodList = MediatorLiveData<Result<List<FoodDomain>>>()
    val foodList: LiveData<Result<List<FoodDomain>>> = liveData {
        emitSource(foodDataRepository.getFoods())
        _status.postValue(View.GONE)//Update the status after the data is loaded
    }

    /*
    Other solution
     val foodList: LiveData<Result<List<FoodDomain>>> = liveData {
        foodDataRepository.getFoods()
    }
     */
    private val _detailFoodEvent = MutableLiveData<Event<FoodDomain>>()
    val detailFoodEvent: LiveData<Event<FoodDomain>>
        get() = _detailFoodEvent

    private val _barcodeFoodEvent = MutableLiveData<Event<Unit>>()
    val barcodeFoodEvent: LiveData<Event<Unit>>
        get() = _barcodeFoodEvent

    private val _newFoodFoodEvent = MutableLiveData<Result<FoodDomain>>()
    val newFoodFoodEvent: LiveData<Result<FoodDomain>>
        get() = _newFoodFoodEvent

    private val _onlineFoodEvent = MutableLiveData<Event<Unit>>()
    val onlineFoodEvent: LiveData<Event<Unit>>
        get() = _onlineFoodEvent

    private val _status = MutableLiveData(View.VISIBLE)
    val status: LiveData<Int>
        get() = _status

    init {
        //initData()
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        println("Exception thrown within parent: $exception.")
    }

    private val childExceptionHandler = CoroutineExceptionHandler{ _, exception ->
        println("Exception thrown in one of the children: $exception.")
    }

    /**
     * Load all the date needed with supervisor. Start listening the foodlist data retireved from db.
     * Every time we update the db it is notified
     */
    @Deprecated("Removed this coroutine and livedata is loaded immediately")
    private fun initData() {
        try {
            viewModelScope.launch {
                 _foodList.addSource(foodDataRepository.getFoods(), _foodList::setValue)
            }.invokeOnCompletion {
                _status.postValue(View.GONE)
            }
        } catch (e: Exception) {
            _status.postValue(View.GONE)
        }
    }

    fun moveToFoodDetail(food: FoodDomain) {
        _detailFoodEvent.value = Event(food)
    }

    fun navigateToBarcodeReader() {
        _barcodeFoodEvent.value = Event(Unit)
    }

    fun navigateToOnlineSearch() {
        _onlineFoodEvent.value = Event(Unit)
    }

    fun getApiFoodDetails(barcode: String) {
        viewModelScope.launch(handler) {
            try {
                _status.value = View.VISIBLE
               supervisorScope {
                   //TODO add flow and use on start to show the loading
                   val apiUdcJob = launch(childExceptionHandler) { foodDataRepository.getApiFoodUpc(barcode) }
               }
            } catch (error: JsonDataException) {
                _newFoodFoodEvent.value = Result.Error(error.toString())
            } catch (generic: Exception) {
                _newFoodFoodEvent.value = Result.Error(generic.toString())
            }
        }.invokeOnCompletion {
            _status.postValue(View.GONE)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
    /*
     * Factory for constructing DevByteViewModel with parameter
     */
    @Deprecated("added DI with hilt")
    class HomeViewModelFactory(
        private val dataRepository: FoodRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(dataRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}