package com.example.savethefood.home

import android.app.Application
import android.util.Log
import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.R
import com.example.savethefood.constants.ApiCallStatus
import com.example.savethefood.constants.ApiCallStatus.*
import com.example.savethefood.data.Result
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.repository.FoodDataRepository
import com.example.savethefood.data.source.repository.FoodRepository
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception

class HomeViewModel @ViewModelInject constructor(
    private val foodDataRepository: FoodRepository
) : ViewModel() {

    val animationResourceButton = R.anim.fade_in

    private val _status = MutableLiveData(View.VISIBLE)
    val status: LiveData<Int>
        get() = _status

    private var _foodList = MediatorLiveData<Result<List<FoodDomain>>>()
    val foodList: LiveData<Result<List<FoodDomain>>> = liveData { //TODO keep live data, but get foods emit, change to flows and collect like https://medium.com/androiddevelopers/livedata-with-coroutines-and-flow-part-iii-livedata-and-coroutines-patterns-592485a4a85a
        emitSource(foodDataRepository.getFoods().asLiveData()) // Change to flow repo and data source, in repo do the oneach, onstart, etc and manage all here with databinding emitSource(foodDataRepository.getFoods().asLiveData())   TO TEST CHANGE CALL TO SCANNER AND ADD MEEDIATELY
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

    private val _bestBeforeFoodEvent = MutableLiveData<Event<Unit>>()
    val bestBeforeFoodEvent: LiveData<Event<Unit>>
        get() = _bestBeforeFoodEvent


    init {
        // TODO, move offer emit, oneanch, catch, map in repository, datasource only suspend
        
        //TODO move to this structure, Result in fragment as observer or databinding (better)??? https://www.droidcon.com/news-detail?content-id=/repository/collaboration/Groups/spaces/droidcon_hq/Documents/public/news/android-news/Using%20LiveData%20and%20Flow%20in%20MVVM%20-%20Part%20II

        //TODO move all live data only in VM, repo and data source with flow (when no one shot) https://proandroiddev.com/no-more-livedata-in-your-repository-there-are-better-options-25a7557b0730

        // TODO use map to convert from flow in every layer (datasoource, repository, vm), It's a good practice for every layer in the app to work with its own model

        //TODO try to use function refenrce where possible like in lambda (if needed add ext functions)
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
                 //_foodList.addSource(foodDataRepository.getFoods(), _foodList::setValue)
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