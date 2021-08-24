package com.example.savethefood.shared.viewmodel

import android.os.Bundle
import androidx.lifecycle.*
import com.example.savethefood.shared.constant.Constants.BUNDLE_FOOD_KEY
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.data.domain.FoodItem
import com.example.savethefood.shared.data.source.repository.FoodRepository
import com.example.savethefood.shared.data.source.repository.RecipeRepository
import com.example.savethefood.shared.utils.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.*
import kotlin.collections.LinkedHashSet


actual class AddFoodViewModel actual constructor(
    private val foodDataRepository: FoodRepository
) : ViewModel() {

    private lateinit var currentState: SavedStateHandle

    constructor(
        foodDataRepository: FoodRepository,
        state: SavedStateHandle
    ) : this(foodDataRepository) {
        with(state) {
            currentState = this
            _foodDomain = get<Bundle>(BUNDLE_FOOD_KEY).retrieveFood()
        }
    }

    private lateinit var _foodDomain: FoodDomain

    private val foodTypeFilter = MutableLiveData<String>()

    private var _foodsItems: LinkedHashSet<FoodItem>? = null
    val foodItems = foodTypeFilter.switchMap { filter ->
        // We need the livedata constructor since _foodItems is not a live data
        // The constructor livedata is also used in case of suspend fun (not flow)
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
    val errorName = MutableLiveData<Boolean>()
    val errorDescription = MutableLiveData<Boolean>()
    val errorPrice = MutableLiveData<Boolean>()
    val errorQuantity = MutableLiveData<Boolean>()
    val errorDate = MutableLiveData<Boolean>()

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