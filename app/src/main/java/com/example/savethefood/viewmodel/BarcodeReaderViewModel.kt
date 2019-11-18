package com.example.savethefood.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.local.domain.FoodDomain
import com.example.savethefood.local.domain.UserDomain
import com.example.savethefood.repository.FoodRepository
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.*

class BarcodeReaderViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = SaveTheFoodDatabase.getInstance(application)
    private val foodRepository = FoodRepository(database)

    private val _food = MutableLiveData<FoodDomain>()
    val food: LiveData<FoodDomain>
        get() = _food
    private val _startReadingBarcode = MutableLiveData<Boolean>()
    val startReadingBarcode: LiveData<Boolean>
        get() = _startReadingBarcode
    private var _popToHome = MediatorLiveData<Boolean>()
    val popToHome: LiveData<Boolean>
        get() = _popToHome
    /*
    If food id has a value of 0 it means that we don't have a valid response from api.
     */
    val validBarcode = Transformations.map(_food) {
        0 != it.foodId
    }

    init {
        _food.value = FoodDomain()
    }
    fun doneReadBarcode() {
        _startReadingBarcode.value = null
    }

    fun getInfoByBarcode() {
        _startReadingBarcode.value = true
    }

    fun getApiFoodDetails(barcode: String) {
        //TODO add binding for progress status
        viewModelScope.launch {
            _food.postValue(foodRepository.getApiFoodUpc(barcode))
            Log.d("Food title", _food.value?.foodTitle)
            Log.d("Food description", _food.value?.foodDescription)
        }
    }

    fun saveFood(food: FoodDomain) {
         viewModelScope.launch {
             foodRepository.saveNewFood(food)
             //TODO check whether the record has been inserted or not
             _popToHome.value = true
         }
    }

    fun donePopToHome() {
        _popToHome.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
    /*
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BarcodeReaderViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BarcodeReaderViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}