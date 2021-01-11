package com.example.savethefood.addfood

import androidx.collection.ArraySet
import androidx.collection.arraySetOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.ui.FoodItem
import com.example.savethefood.util.FoodImage


class AddFoodViewModel(

) : ViewModel() {

    var foodItem = FoodDomain()

    var selectedItem = MutableLiveData<FoodItem>()
        set(value) {
            field = value
            foodItem.foodImg = value.value?.img ?: FoodImage.EMPTY
        }

    private var _foodsItems = arraySetOf<FoodItem>()

    val foodItems: ArraySet<FoodItem>
        get() = _foodsItems

    init {
        _foodsItems = getCustomSet()
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
}