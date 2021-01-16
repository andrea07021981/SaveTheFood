package com.example.savethefood.addfood

import androidx.collection.ArraySet
import androidx.collection.arraySetOf
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.ui.FoodItem
import com.example.savethefood.util.FoodImage


class AddFoodViewModel(

) : ViewModel() {

    var foodItem = FoodDomain()
    // TODO use this one when dialog close or pass foodItem to dialog
    var selectedItem = MutableLiveData<FoodItem>()
        set(value) {
            field = value
            foodItem.foodImg = value.value?.img ?: FoodImage.EMPTY
        }

    var foodTypeFilter = MutableLiveData("")

    private val _foodTypeFilter: MutableLiveData<String>
        get() = foodTypeFilter

    private var _foodsItems: ArraySet<FoodItem>? = null

    // TODO review it
    val foodItems: LiveData<Set<FoodItem>> = _foodTypeFilter.switchMap { filter ->
        liveData {
            if (filter.isNullOrEmpty()) {
                _foodsItems
            } else {
                _foodsItems?.filter {
                    it.name.contains(filter)
                }
            }
        }
    }

    init {
        _foodsItems = getCustomSet()
    }

    private val _openFoodTypeDialog = MutableLiveData<Event<Unit>>()
    val openFoodTypeDialog: LiveData<Event<Unit>>
        get() = _openFoodTypeDialog

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

    fun openFoodDialog() {
        _openFoodTypeDialog.value = Event(Unit)
    }
}