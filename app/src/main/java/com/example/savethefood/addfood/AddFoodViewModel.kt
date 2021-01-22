package com.example.savethefood.addfood

import android.util.Log
import androidx.collection.ArraySet
import androidx.collection.arraySetOf
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.FoodItem
import com.example.savethefood.util.FoodImage
import java.util.*


class AddFoodViewModel(

) : ViewModel() {

    private val _foodItem = MutableLiveData(FoodItem(FoodImage.EMPTY))
    val foodItem: LiveData<FoodItem>
        get() = _foodItem

    private val _foodDomain = FoodDomain()
    val foodDomain: LiveData<FoodDomain> = Transformations.map(_foodItem) {
        _foodDomain.apply {
            foodImg = it.img
        }
    }

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