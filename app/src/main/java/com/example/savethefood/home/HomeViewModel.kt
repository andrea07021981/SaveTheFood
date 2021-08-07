package com.example.savethefood.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.R
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.utils.Event
import com.example.savethefood.shared.utils.FoodOrder
import com.example.savethefood.shared.utils.StorageType
import com.example.savethefood.shared.utils.customSortBy
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

// TODO use homwviewmodel for edit and add? save resources
class HomeViewModel @ViewModelInject constructor(
    private val foodDataRepository: com.example.savethefood.shared.data.source.repository.FoodRepository
) : ViewModel() {

    /**
     * This filter class helps to fire the switchmap when we change the filter type or order
     * The issue was that wee can't use two livedata switchmap to refresh one single livedata
     */
    private sealed class FoodFilters {
        class Filter(val value: String): FoodFilters()
        class Order(val order: FoodOrder): FoodFilters()
        object None: FoodFilters()
    }

    private val _storageType = MutableLiveData(StorageType.ALL)
    val storageType: LiveData<StorageType>
        get() = _storageType

    private val _errorData = MutableLiveData<Event<String>>()
    val errorData: LiveData<Event<String>>
        get() = _errorData

    private val searchFilter = MutableLiveData<FoodFilters>(FoodFilters.None)

    private val _listOrderEvent = MutableLiveData<Event<FoodOrder>>()
    val listOrderEvent: LiveData<Event<FoodOrder>>
        get() = _listOrderEvent

    private var _foodList: LiveData<List<FoodDomain>?> = foodDataRepository.getFoods()
        .onStart {
            com.example.savethefood.shared.data.Result.Loading
        }
        .catch { error ->
            emit(com.example.savethefood.shared.data.Result.ExError(java.lang.Exception(error.message)))
        }
        .transform { value ->
            when (value) {
                is com.example.savethefood.shared.data.Result.Loading -> Unit // TODO animate list
                is com.example.savethefood.shared.data.Result.Success -> emit(value.data)
                is com.example.savethefood.shared.data.Result.ExError -> _errorData.value = Event(value.exception.localizedMessage)
                else -> Unit
            }
        }
        .onCompletion {
            // Never called since the channel with room is always open to listen
        }
        .asLiveData(viewModelScope.coroutineContext)

    val foodList: LiveData<List<FoodDomain>?> = Transformations.distinctUntilChanged(
        searchFilter.switchMap {
            when (it) {
                is FoodFilters.Filter -> {
                    if (it.value.isNotEmpty()) {
                        _foodList.map { list ->
                            list?.filter { recipe ->
                                recipe.title.toLowerCase(Locale.getDefault())
                                    .contains(it.value.toLowerCase(Locale.getDefault()))
                            }
                        }
                    } else {
                        _foodList
                    }
                }
                is FoodFilters.Order -> {
                    if (it.order != FoodOrder.NONE) {
                        _foodList.map { list ->
                            list?.customSortBy(it.order)
                        }
                    } else {
                        _foodList
                    }
                }
                else -> _foodList
            }
        }
    )

    private val _detailFoodEvent = MutableLiveData<Event<FoodDomain>>()
    val detailFoodEvent: LiveData<Event<FoodDomain>>
        get() = _detailFoodEvent

    private val _addFoodEvent = MutableLiveData<Event<Unit>>()
    val addFoodEvent: LiveData<Event<Unit>>
        get() = _addFoodEvent

    /**
     * Observer for the storagetype and numbers of each.
     * Creates a map for all types if the list is empty
     */
    val listByStorageType: LiveData<Map<StorageType, Int>?> = _foodList.map { result ->
        if (result?.size != 0) {
            result?.groupingBy(FoodDomain::storageType)?.eachCount()?.toMutableMap().also {
                it?.set(StorageType.ALL, result?.count() ?: 0)
            }
        } else {
            StorageType.values().map {
                it to 0
            }.toMap()
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

    fun updateDataList(filter: String) {
        searchFilter.value = FoodFilters.Filter(filter)
    }

    fun updateDataList(order: FoodOrder) {
        searchFilter.value = FoodFilters.Order(order)
    }

    fun updateDataListEvent(order: FoodOrder) {
        _listOrderEvent.value = Event(order)
    }
}