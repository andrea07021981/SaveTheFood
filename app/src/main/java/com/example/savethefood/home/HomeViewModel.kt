package com.example.savethefood.home

import android.app.Application
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.R
import com.example.savethefood.data.Result
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.repository.FoodDataRepository
import com.example.savethefood.data.source.repository.FoodRepository
import kotlinx.coroutines.*

class HomeViewModel(
    private val foodDataRepository: FoodRepository
) : ViewModel() {

    val animationResourceButton = R.anim.fade_in

    //TODO remove viewModelJob and scope, with new version of androidx there the viewmodescope of
    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this scope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    //Supervisor prevent a crash. If one on the children fails, it keeps working.
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _foodList = MediatorLiveData<Result<List<FoodDomain>>>()
    val foodList: LiveData<Result<List<FoodDomain>>>
        get() = _foodList

    private val _detailFoodEvent = MutableLiveData<Event<FoodDomain>>()
    val detailFoodEvent: LiveData<Event<FoodDomain>>
        get() = _detailFoodEvent

    private val _barcodeFoodEvent = MutableLiveData<Event<Unit>>()
    val barcodeFoodEvent: LiveData<Event<Unit>>
        get() = _barcodeFoodEvent

    private val _onlineFoodEvent = MutableLiveData<Event<Unit>>()
    val onlineFoodEvent: LiveData<Event<Unit>>
        get() = _onlineFoodEvent

    init {
        viewModelScope.launch {
            _foodList.addSource(foodDataRepository.getFoods(), _foodList::setValue)
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

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
    /*
     * Factory for constructing DevByteViewModel with parameter
     */
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