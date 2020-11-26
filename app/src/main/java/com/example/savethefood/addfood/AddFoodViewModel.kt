package com.example.savethefood.addfood

import androidx.collection.ArraySet
import androidx.collection.arraySetOf
import androidx.lifecycle.ViewModel
import com.example.savethefood.ui.FoodItem
import com.example.savethefood.util.FoodImage

class AddFoodViewModel(

) : ViewModel() {

    private var _foodsItems = arraySetOf<FoodItem>()
    val foodItems: ArraySet<FoodItem>
        get() = _foodsItems

    var foodItem = 3

    init {
        _foodsItems = getCustomSet()
    }

    private fun getCustomSet(): ArraySet<FoodItem> {
        val customObjects = arraySetOf<FoodItem>()
        customObjects.apply {
            FoodImage.values().forEach {
                this.add(FoodItem(it.name, it))
            }
        }
        return customObjects
    }
}