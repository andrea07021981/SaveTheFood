package com.example.savethefood.util

import com.example.savethefood.data.domain.FoodItem
import java.util.*

/**
 * Class to notify the main food domain when something changes
 */
sealed class ObserverFormFields {
    class FoodItemImage(val value: FoodItem = FoodItem(FoodImage.EMPTY)): ObserverFormFields()
    class BestBefore(val value: Date?): ObserverFormFields()
    object None: ObserverFormFields()
}
