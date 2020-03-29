package com.example.savethefood.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.savethefood.R
import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.local.domain.FoodDomain
import com.example.savethefood.repository.FoodRepository
import kotlinx.coroutines.*

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    val animationResourceButton = R.anim.bounce

    //Supervisor prevent a crash. If one on the children fails, it keeps working.
    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = SaveTheFoodDatabase.getInstance(application)
    private val foodsRepository = FoodRepository(database)

    private var _foodList = MediatorLiveData<List<FoodDomain>>()
    val foodList: LiveData<List<FoodDomain>>
        get() = _foodList

    private var _navigateToFoodDetail = MediatorLiveData<FoodDomain>()
    val navigateToFoodDetail: LiveData<FoodDomain>
        get() = _navigateToFoodDetail

    private var _navigateToBarcodeReader = MediatorLiveData<Boolean>()
    val navigateToBarcodeReader: LiveData<Boolean>
        get() = _navigateToBarcodeReader

    init {
        viewModelScope.launch {
            _foodList.addSource(foodsRepository.getFoods(), _foodList::setValue)
        }
    }

    fun moveToFoodDetail(food: FoodDomain) {
        _navigateToFoodDetail.value = food
    }

    fun doneToFoodDetail() {
        _navigateToFoodDetail.value = null
    }

    fun moveToBarcodeReader() {
        _navigateToBarcodeReader.value = true
    }

    fun doneToBarcodeReader() {
        _navigateToBarcodeReader.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
    /*
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}