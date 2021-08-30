package com.example.savethefood.shared.viewmodel

import android.os.Bundle
import androidx.lifecycle.*
import com.example.savethefood.shared.constant.Constants
import com.example.savethefood.shared.data.domain.BagDomain
import com.example.savethefood.shared.data.domain.CategoryItem
import com.example.savethefood.shared.data.domain.FoodItem
import com.example.savethefood.shared.data.source.repository.FoodRepository
import com.example.savethefood.shared.data.source.repository.ShoppingRepository
import com.example.savethefood.shared.utils.*
import java.util.*
import kotlin.collections.LinkedHashSet


actual class BagDetailViewModel actual constructor(
    private val shoppingDataRepository: ShoppingRepository,
    private val foodDataRepository: FoodRepository
) : ViewModel(){

    constructor(
        shoppingDataRepository: ShoppingRepository,
        foodDataRepository: FoodRepository,
        state: SavedStateHandle
    ) : this(shoppingDataRepository, foodDataRepository) {
        _foodBag = state.get<Bundle>(Constants.BUNDLE_BAG_KEY).retrieveBag()
    }

    private var _foodBag: BagDomain = BagDomain()

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

    private var _categoryItems: LinkedHashSet<CategoryItem>? = null
    val categoryItems: LinkedHashSet<CategoryItem>?
        get() = _categoryItems

    init {
        _categoryItems = shoppingDataRepository.getFoodCategories()
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