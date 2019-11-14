package com.example.savethefood.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.network.service.FoodApi
import com.example.savethefood.repository.FoodRepository
import com.example.savethefood.repository.UserRepository
import kotlinx.coroutines.*
import java.lang.Exception

class BarcodeReaderViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = SaveTheFoodDatabase.getInstance(application)
    private val foodRepository = FoodRepository(database)

    private val _getInfoByBarcode = MutableLiveData<Boolean>()
    val getInfoByBarcode: LiveData<Boolean>
        get() = _getInfoByBarcode

    fun doneReadBarcode() {
        _getInfoByBarcode.value = null
    }

    fun getInfoByBarcode() {
        viewModelScope.launch {
            foodRepository.getApiFoodUpc()
        }
        _getInfoByBarcode.value = true
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