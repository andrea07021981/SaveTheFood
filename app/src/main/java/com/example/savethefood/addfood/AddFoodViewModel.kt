package com.example.savethefood.addfood

import android.os.Bundle
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.constants.Constants.BUNDLE_FOOD_KEY
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.data.domain.FoodItem
import com.example.savethefood.shared.utils.Event
import com.example.savethefood.shared.utils.isValidDouble
import com.example.savethefood.util.ObserverFormFields
import com.example.savethefood.util.launchDataLoad
import com.example.savethefood.util.retrieveFood
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.*
import kotlin.collections.LinkedHashSet


@Deprecated("Moved to shared")
class AddFoodViewModel @ViewModelInject constructor(
    private val foodDataRepository: com.example.savethefood.shared.data.source.repository.FoodRepository,
    @Assisted val food: SavedStateHandle
) : ViewModel() {

    private val _foodDomain: FoodDomain = food.get<Bundle>(BUNDLE_FOOD_KEY).retrieveFood()

    private val foodTypeFilter = MutableLiveData<String>()

    private var _foodsItems: LinkedHashSet<FoodItem>? = null
    // We could replace with map
    val foodItems = foodTypeFilter.switchMap { filter ->
        // We need the livedata constructor since _foodItems is not a live data
        // The constructor livedata is also used in case of suspend fun (not flow)
        liveData {
            if (filter.isNullOrEmpty()) {
                emit(_foodsItems)
            } else {
                emit(_foodsItems?.filter {
                    it.name.contains(filter.uppercase(Locale.getDefault()))
                })
            }
        }
    }

    // OR using map
/*    val foodItems = foodTypeFilter.map { filter ->
        // We need the livedata constructor since _foodItems is not a live data
        // The constructor livedata is also used in case of suspend fun (not flow)
        if (filter.isNullOrEmpty()) {
            _foodsItems
        } else {
            _foodsItems?.filter {
                it.name.contains(filter.uppercase(Locale.getDefault()))
            }
        }
    }*/

    init {
        _foodsItems = foodDataRepository.getFoodImages()
    }

    private var observerField: MutableLiveData<ObserverFormFields> = MutableLiveData(ObserverFormFields.FoodItemImage(
        FoodItem(_foodDomain.img)
    ))

    val foodDomain: LiveData<FoodDomain> = Transformations.map(observerField) {
        _foodDomain.apply {
            when (it) {
                is ObserverFormFields.FoodItemImage -> img = it.value.img
                is ObserverFormFields.BestBefore -> bestBefore = it.value?.time ?: Date().time
                else -> Unit
            }
        }
    }

    // TODO use custom set for livedata or transformations to update
    private val errorName = MutableLiveData<Boolean>()
    private val errorDescription = MutableLiveData<Boolean>()
    private val errorPrice = MutableLiveData<Boolean>()
    private val errorQuantity = MutableLiveData<Boolean>()
    private val errorDate = MutableLiveData<Boolean>()

    private val _barcodeFoodEvent = MutableLiveData<Event<Unit>>()
    val barcodeFoodEvent: LiveData<Event<Unit>>
        get() = _barcodeFoodEvent

    private val _saveFoodEvent = MutableLiveData<com.example.savethefood.shared.data.Result<FoodDomain>>()
    val saveFoodEvent: LiveData<com.example.savethefood.shared.data.Result<FoodDomain>>
        get() = _saveFoodEvent

    private val _newFoodFoodEvent = MutableLiveData<com.example.savethefood.shared.data.Result<FoodDomain>>()
    val newFoodFoodEvent: LiveData<com.example.savethefood.shared.data.Result<FoodDomain>>
        get() = _newFoodFoodEvent

    private val _openFoodTypeDialog = MutableLiveData<Event<Unit>>()
    val openFoodTypeDialog: LiveData<Event<Unit>>
        get() = _openFoodTypeDialog

    private val _openDateDialog = MutableLiveData<Event<Date>>()
    val openDateDialog: LiveData<Event<Date>>
        get() = _openDateDialog

    @Deprecated("Removed the datepicker")
    val updateBestBefore: (Calendar) -> Unit = { date ->
        _foodDomain.bestBefore = date.time.time
    }

    fun updateFilter(filter: String) {
        foodTypeFilter.value = filter
    }

    fun updateFood(foodItem: FoodItem) {
        observerField.value = ObserverFormFields.FoodItemImage(foodItem)
    }

    fun updateBestBefore(date: Date) {
        observerField.value = ObserverFormFields.BestBefore(date)
    }

    fun save() {
        errorName.value = _foodDomain.title.isNullOrBlank()
        errorDescription.value = _foodDomain.description.isNullOrBlank()
        errorPrice.value = _foodDomain.price.isValidDouble().not()
        errorQuantity.value = _foodDomain.quantity.isValidDouble().not()
        errorDate.value = _foodDomain.bestBefore == null

        if (errorName.value == false &&
            errorDescription.value == false &&
            errorPrice.value == false &&
            errorQuantity.value == false &&
            errorDate.value == false
        ) {
            launchDataLoad(_saveFoodEvent) {
                foodDataRepository.saveNewFood(_foodDomain)
            }
        }
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
                _newFoodFoodEvent.value = com.example.savethefood.shared.data.Result.Error(error.toString())
            } catch (generic: Exception) {
                _newFoodFoodEvent.value = com.example.savethefood.shared.data.Result.Error(generic.toString())
            }
        }.invokeOnCompletion {
        }
    }

    fun openFoodDialog() {
        _openFoodTypeDialog.value = Event(Unit)
    }

    fun openDateDialog() {
        _openDateDialog.value = Event(Date(_foodDomain.bestBefore ?: 0)) // TODO fix zero
    }

    fun navigateToBarcodeReader() {
        _barcodeFoodEvent.value = Event(Unit)
    }
}