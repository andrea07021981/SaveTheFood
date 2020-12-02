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

    private var _foodsItems = arraySetOf<FoodItem>()

    val foodItems: ArraySet<FoodItem>
        get() = _foodsItems

    // TODO use two way databinding and change this way with observable
    var spinnerSelectedPosition: Int = 0
        set(value) {
            field = value
            foodItem.foodImg = _foodsItems.elementAt(2).img
        }

    init {
        _foodsItems = getCustomSet()
    }

    // TO change to sorted set or treeset
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