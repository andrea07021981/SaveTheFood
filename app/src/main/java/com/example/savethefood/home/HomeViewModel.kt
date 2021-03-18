package com.example.savethefood.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.R
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.repository.FoodRepository
import com.example.savethefood.util.StorageType
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

// TODO use homwviewmodel for edit and add? save resources
class HomeViewModel @ViewModelInject constructor(
    private val foodDataRepository: FoodRepository
) : ViewModel() {

    val animationResourceButton = R.anim.fade_in

    private val _storageType = MutableLiveData<StorageType>(StorageType.ALL)
    val storageType: LiveData<StorageType>
        get() = _storageType

    private val _errorData = MutableLiveData<Event<String>>()
    val errorData: LiveData<Event<String>>
        get() = _errorData

    private var _foodList: LiveData<List<FoodDomain>?> = foodDataRepository.getFoods()
        .transform { value ->
            when (value) {
                is Result.Success -> emit(value.data)
                is Result.ExError -> _errorData.value = Event(value.exception.localizedMessage)
                else -> Unit
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    val foodList: LiveData<List<FoodDomain>?>
        get() = _foodList

    private val _detailFoodEvent = MutableLiveData<Event<FoodDomain>>()
    val detailFoodEvent: LiveData<Event<FoodDomain>>
        get() = _detailFoodEvent

    private val _addFoodEvent = MutableLiveData<Event<Unit>>()
    val addFoodEvent: LiveData<Event<Unit>>
        get() = _addFoodEvent

    val listByStorageType: LiveData<Map<StorageType, Int>?> = _foodList.map { result ->
        result?.groupingBy(FoodDomain::storageType)?.eachCount()?.toMutableMap().also {
            it?.set(StorageType.ALL, result?.count() ?: 0)
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
}