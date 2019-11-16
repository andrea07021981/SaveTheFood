package com.example.savethefood.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.local.domain.FoodDomain
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

    var barcodeValue = MutableLiveData<String>()

    private val _startReadingBarcode = MutableLiveData<Boolean>()
    val startReadingBarcode: LiveData<Boolean>
        get() = _startReadingBarcode

    init {
        //TODO use variable for binding adapter and disable components until we get an error from barcode
    }
    fun doneReadBarcode() {
        _startReadingBarcode.value = null
    }

    fun getInfoByBarcode() {
        _startReadingBarcode.value = true
    }

    fun saveFood(food: FoodDomain) {
        //TODO start scanning
        _startReadingBarcode.value = true

        /* viewModelScope.launch {
             foodRepository.getApiFoodUpc()
         }*/
    }

    fun doneStartReadingBarcode() {
        _startReadingBarcode.value = null
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