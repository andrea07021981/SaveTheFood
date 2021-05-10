package com.example.savethefood.addfood

import android.os.Bundle
import androidx.collection.ArraySet
import androidx.collection.arraySetOf
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.constants.Constants.BUNDLE_FOOD_KEY
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.FoodItem
import com.example.savethefood.data.source.repository.FoodRepository
import com.example.savethefood.util.FoodImage
import com.example.savethefood.util.isValidDouble
import com.example.savethefood.util.launchDataLoad
import com.example.savethefood.util.retrieveFood
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.*
import kotlin.collections.LinkedHashSet


class AddFoodViewModel @ViewModelInject constructor(
    private val foodDataRepository: FoodRepository,
    @Assisted val food: SavedStateHandle
) : ViewModel() {

    private val _foodDomain: FoodDomain = food.get<Bundle>(BUNDLE_FOOD_KEY).retrieveFood()

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
        _foodsItems = getCustomSet()
    }

    /**
     * Private class to notify the main food domain when something changes
     */
    private sealed class ObserverFields {
        class FoodItemImage(val value: FoodItem = FoodItem(FoodImage.EMPTY)): ObserverFields()
        class BestBefore(val value: Date?): ObserverFields()
        object None: ObserverFields()
    }

    private var observerField: MutableLiveData<ObserverFields> = MutableLiveData(ObserverFields.FoodItemImage(
        FoodItem(_foodDomain.img)
    ))

    val foodDomain: LiveData<FoodDomain> = Transformations.map(observerField) {
        _foodDomain.apply {
            when (it) {
                is ObserverFields.FoodItemImage -> img = it.value.img
                is ObserverFields.BestBefore -> bestBefore = it.value ?: Date()
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

    private val _saveFoodEvent = MutableLiveData<Result<FoodDomain>>()
    val saveFoodEvent: LiveData<Result<FoodDomain>>
        get() = _saveFoodEvent

    private val _newFoodFoodEvent = MutableLiveData<Result<FoodDomain>>()
    val newFoodFoodEvent: LiveData<Result<FoodDomain>>
        get() = _newFoodFoodEvent

    private val _openFoodTypeDialog = MutableLiveData<Event<Unit>>()
    val openFoodTypeDialog: LiveData<Event<Unit>>
        get() = _openFoodTypeDialog

    private val _openDateDialog = MutableLiveData<Event<Date>>()
    val openDateDialog: LiveData<Event<Date>>
        get() = _openDateDialog

    @Deprecated("Removed the datepicker")
    val updateBestBefore: (Calendar) -> Unit = { date ->
        _foodDomain.bestBefore = date.time
    }

    fun updateFilter(filter: String) {
        foodTypeFilter.value = filter
    }

    fun updateFood(foodItem: FoodItem) {
        observerField.value = ObserverFields.FoodItemImage(foodItem)
    }

    fun updateBestBefore(date: Date) {
        observerField.value = ObserverFields.BestBefore(date)
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

    /**
     * Create a LinkedHashset, we don't have duplicates but must keep the order
     */
    private fun getCustomSet(): LinkedHashSet<FoodItem> {
        val customObjects = linkedSetOf<FoodItem>()
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

    fun openDateDialog() {
        _openDateDialog.value = Event(_foodDomain.bestBefore ?: Date())
    }

    fun navigateToBarcodeReader() {
        _barcodeFoodEvent.value = Event(Unit)
    }
}