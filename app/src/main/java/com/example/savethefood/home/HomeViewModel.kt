package com.example.savethefood.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.R
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.repository.FoodRepository
import com.example.savethefood.util.StorageType
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.onCompletion
import java.lang.Exception
import java.util.*

class HomeViewModel @ViewModelInject constructor(
    private val foodDataRepository: FoodRepository
) : ViewModel() {

    val animationResourceButton = R.anim.fade_in

    //TODO like plantrepository in advanced coroutine codelab
    //val foodList: LiveData<Result<List<FoodDomain>>> = liveData { //TODO keep live data, but get foods emit, change to flows and collect like https://medium.com/androiddevelopers/livedata-with-coroutines-and-flow-part-iii-livedata-and-coroutines-patterns-592485a4a85a
    //    emitSource(foodDataRepository.getFoods().asLiveData(Dispatchers.IO)) // Change to flow repo and data source, in repo do the oneach, onstart, etc and manage all here with databinding emitSource(foodDataRepository.getFoods().asLiveData())   TO TEST CHANGE CALL TO SCANNER AND ADD MEEDIATELY
    //}
   // TODO can also use dTransformations distinct https://proandroiddev.com/livedata-transformations-4f120ac046fc
    /*
    Other solution
     val foodList: LiveData<Result<List<FoodDomain>>> = liveData {
        foodDataRepository.getFoods()
    }
     */

    private val _storageType = MutableLiveData<StorageType>(StorageType.ALL)
    private var _foodList: LiveData<Result<List<FoodDomain>>> = foodDataRepository.getFoods()
        .onCompletion {
            // TODO hide here the spinner instead of using the result
        }
        .asLiveData(viewModelScope.coroutineContext)
    val foodList: LiveData<Result<List<FoodDomain>>> = Transformations.distinctUntilChanged(
        _storageType.switchMap {
            _foodList // TODO filter with storage type, can we remove the result with transform? need it of the loader
        }
    )
    private val _detailFoodEvent = MutableLiveData<Event<FoodDomain>>()
    val detailFoodEvent: LiveData<Event<FoodDomain>>
        get() = _detailFoodEvent

    private val _addFoodEvent = MutableLiveData<Event<Unit>>()
    val addFoodEvent: LiveData<Event<Unit>>
        get() = _addFoodEvent

    val storageAllCount: LiveData<Int> = _foodList.map { result ->
        if (result is Result.Success) {
            result.data.count { it != null }
        } else {
            0
        }
    }

    fun storageTypeCount(storageType: StorageType): LiveData<Int> {
        return Transformations.map(_foodList) { result ->
            if (result is Result.Success) {
                result.data.count { it.storageType == storageType }
            } else {
                0
            }
        }
    }

    val storageFridgeCount: LiveData<Int> = Transformations.map(_foodList) { result ->
        if (result is Result.Success) {
            result.data.count { it.storageType == StorageType.ALL }
        } else {
            0
        }
    }

    val storageFreezerCount: LiveData<Int> = Transformations.map(_foodList) { result ->
        if (result is Result.Success) {
            result.data.count { it.storageType == StorageType.ALL }
        } else {
            0
        }
    }

    val storageDryCount: LiveData<Int> = Transformations.map(_foodList) { result ->
        if (result is Result.Success) {
            result.data.count { it.storageType == StorageType.ALL }
        } else {
            0
        }
    }

    init {
        // TODO, move offer emit, oneanch, catch, map in repository, datasource only suspend
        // with states, we can do .catch, oneach, etc in repo and emit errors or other.
        // Then the collect with start everything and using a class like Result will store data, errors, etc
        // We can also decide to do the intermediate operations here, maybe easier?

        // 1 in repo    -> Use emit
        // 2 in VM      -> Create functions in VM

        //TODO move to this structure, Result in fragment as observer or databinding (better)??? https://www.droidcon.com/news-detail?content-id=/repository/collaboration/Groups/spaces/droidcon_hq/Documents/public/news/android-news/Using%20LiveData%20and%20Flow%20in%20MVVM%20-%20Part%20II

        //TODO move all live data only in VM, repo and data source with flow (when no one shot) https://proandroiddev.com/no-more-livedata-in-your-repository-there-are-better-options-25a7557b0730

        // TODO use map to convert from flow in every layer (datasoource, repository, vm), It's a good practice for every layer in the app to work with its own model

        //TODO try to use function refenrce where possible like in lambda (if needed add ext functions)
        //initData()
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
            }
        } catch (e: Exception) {
        }
    }

    fun moveToFoodDetail(food: FoodDomain) {
        _detailFoodEvent.value = Event(food)
    }

    fun navigateToAddFood() {
        _addFoodEvent.value = Event(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun updateIndex(storageType: StorageType) {
        // use switch map like
        _storageType.value = storageType
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