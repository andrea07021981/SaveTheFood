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

    private val _foodItem = MutableLiveData(FoodDomain())
    val foodItem: LiveData<FoodDomain>
        get() = _foodItem

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

    fun updateFilter(filter: String) {
        foodTypeFilter.value = filter
    }

    fun updateFood(foodItem: FoodItem) {
        // TODO search if we can use bindable and observable.
        // MutableLiveData notify only when the object change
        _foodItem.value = _foodItem.value?.copy(foodImg = foodItem.img)
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

    fun openFoodDialog() {
        _openFoodTypeDialog.value = Event(Unit)
    }
}