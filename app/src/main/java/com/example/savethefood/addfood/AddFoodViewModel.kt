package com.example.savethefood.addfood

import androidx.collection.ArraySet
import androidx.collection.arraySetOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.FoodItem
import com.example.savethefood.data.source.repository.FoodRepository
import com.example.savethefood.util.FoodImage
import com.example.savethefood.util.StorageType
import com.example.savethefood.util.isValidDouble
import com.example.savethefood.util.launchDataLoad
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.lang.Exception
import java.util.*


class AddFoodViewModel @ViewModelInject constructor(
    private val foodDataRepository: FoodRepository
) : ViewModel() {

    private val _foodItem = MutableLiveData(FoodItem(FoodImage.EMPTY))
    val foodItem: LiveData<FoodItem>
        get() = _foodItem

    private val _foodDomain = FoodDomain()
    val foodDomain: LiveData<FoodDomain> = Transformations.map(_foodItem) {
        _foodDomain.apply {
            img = it.img
        }
    }

    // TODO use custom set for livedata or transformations to update
    val errorName = MutableLiveData<Boolean>()
    val errorDescription = MutableLiveData<Boolean>()
    val errorPrice = MutableLiveData<Boolean>()
    val errorQuantity = MutableLiveData<Boolean>()

    private val _barcodeFoodEvent = MutableLiveData<Event<Unit>>()
    val barcodeFoodEvent: LiveData<Event<Unit>>
        get() = _barcodeFoodEvent

    private val _saveFoodEvent = MutableLiveData<Result<FoodDomain>>()
    val saveFoodEvent: LiveData<Result<FoodDomain>>
        get() = _saveFoodEvent

    private val _newFoodFoodEvent = MutableLiveData<Result<FoodDomain>>()
    val newFoodFoodEvent: LiveData<Result<FoodDomain>>
        get() = _newFoodFoodEvent

    private val foodTypeFilter = MutableLiveData<String>()
    private var _foodsItems: ArraySet<FoodItem>? = null
    val foodItems = foodTypeFilter.switchMap { filter ->
        liveData {
            if (filter.isNullOrEmpty()) {
                emit(_foodsItems)
            } else {
                emit(_foodsItems?.filter {
                    it.name.contains(filter.toUpperCase())
                })
            }
        }
    }

    init {
        _foodsItems = getCustomSet()
    }

    private val _openFoodTypeDialog = MutableLiveData<Event<Unit>>()
    val openFoodTypeDialog: LiveData<Event<Unit>>
        get() = _openFoodTypeDialog


    val updateBestBefore: (Calendar) -> Unit = { date ->
        _foodDomain.bestBefore = date.time
    }

    fun updateFilter(filter: String) {
        foodTypeFilter.value = filter
    }

    fun updateFood(foodItem: FoodItem) {
        _foodItem.value = foodItem
    }

    fun save() {
        errorName.value = _foodDomain.title.isNullOrBlank()
        errorDescription.value = _foodDomain.description.isNullOrBlank()
        errorPrice.value = _foodDomain.price.isValidDouble().not()
        errorQuantity.value = _foodDomain.quantity.isValidDouble().not()

        if (errorName.value == false &&
            errorDescription.value == false &&
            errorPrice.value == false &&
            errorQuantity.value == false
        ) {
            launchDataLoad(_saveFoodEvent) {
                foodDataRepository.saveNewFood(_foodDomain)
            }
        }
    }

    // TODO change to sorted set or treeset
    private fun getCustomSet(): ArraySet<FoodItem> {
        val customObjects = arraySetOf<FoodItem>()
        customObjects
            .apply {
                FoodImage.values()
                    .sortedBy(FoodImage::name)
                    .forEach {
                        this.add(FoodItem(it.name, it))
            }
        }
        return customObjects
    }


    private val handler = CoroutineExceptionHandler { _, exception ->
        println("Exception thrown within parent: $exception.")
    }

    private val childExceptionHandler = CoroutineExceptionHandler{ _, exception ->
        println("Exception thrown in one of the children: $exception.")
    }

    fun getApiFoodDetails(barcode: String) {
        viewModelScope.launch(handler) {
            try {
                supervisorScope {
                    val foodJob = launch(childExceptionHandler) {
                        _newFoodFoodEvent.value = foodDataRepository.getApiFoodUpc(barcode)
                    }
                    foodJob.join()
                }
            } catch (error: JsonDataException) {
                _newFoodFoodEvent.value = Result.Error(error.toString())
            } catch (generic: Exception) {
                _newFoodFoodEvent.value = Result.Error(generic.toString())
            }
        }.invokeOnCompletion {
        }
    }

    fun openFoodDialog() {
        _openFoodTypeDialog.value = Event(Unit)
    }

    fun navigateToBarcodeReader() {
        _barcodeFoodEvent.value = Event(Unit)
    }
}