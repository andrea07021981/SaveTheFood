package com.example.savethefood.shopping

import android.os.Bundle
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.constants.Constants
import com.example.savethefood.shared.data.domain.BagDomain
import com.example.savethefood.shared.data.domain.FoodItem
import com.example.savethefood.shared.utils.Event
import com.example.savethefood.util.ObserverFormFields
import com.example.savethefood.util.isValidDouble
import com.example.savethefood.util.launchDataLoad
import com.example.savethefood.util.retrieveBag
import java.util.*
import kotlin.collections.LinkedHashSet


@Deprecated("Moved to shared")
class BagDetailViewModel @ViewModelInject constructor(
    private val shoppingDataRepository: com.example.savethefood.shared.data.source.repository.ShoppingRepository,
    private val foodDataRepository: com.example.savethefood.shared.data.source.repository.FoodRepository,
    @Assisted val bag: SavedStateHandle
) : ViewModel(){

    private var _foodBag: BagDomain = bag.get<Bundle>(Constants.BUNDLE_BAG_KEY).retrieveBag()

    private var _bagDetailEvent = MutableLiveData<Event<BagDomain>>()
    val bagDetailEvent = _bagDetailEvent

    private val _openFoodTypeDialog = MutableLiveData<Event<Unit>>()
    val openFoodTypeDialog: LiveData<Event<Unit>>
        get() = _openFoodTypeDialog

    private val _saveFoodEvent = MutableLiveData<com.example.savethefood.shared.data.Result<BagDomain>>()
    val saveFoodEvent: LiveData<com.example.savethefood.shared.data.Result<BagDomain>>
        get() = _saveFoodEvent

    val errorName = MutableLiveData<Boolean>()
    val errorQuantity = MutableLiveData<Boolean>()

    // TODO This code is duplicated with addfood, solve it
    // §§TODO§ SearchFragment uses addviewmodel, fix it
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

    private var observerField: MutableLiveData<ObserverFormFields> = MutableLiveData(
        ObserverFormFields.FoodItemImage(
            FoodItem(_foodBag.img)
        )
    )

    val bagDomain: LiveData<BagDomain> = observerField.map {
        _foodBag.apply {
            when (it) {
                is ObserverFormFields.FoodItemImage -> img = it.value.img
                else -> Unit
            }
        }
    }

    init {
        _foodsItems = foodDataRepository.getFoodImages()
    }

    fun save() {
        errorName.value = _foodBag.title.isBlank()
        errorQuantity.value = _foodBag.quantity.isValidDouble().not()

        if (errorName.value == false && errorQuantity.value == false) {
            launchDataLoad(_saveFoodEvent) {
                shoppingDataRepository.saveItemInBag(_foodBag)
            }
        }
    }

    fun openFoodDialog() {
        _openFoodTypeDialog.value = Event(Unit)
    }

    fun updateFood(foodItem: FoodItem) {
        observerField.value = ObserverFormFields.FoodItemImage(foodItem)
    }
}