package com.example.savethefood.shared.utils

import com.example.savethefood.shared.data.domain.FoodItem
import com.example.savethefood.shared.utils.FoodImage

/**
 * Class to notify the main food domain when something changes
 */
sealed class ObserverFormFields {
    class FoodItemImage(val value: FoodItem = FoodItem(FoodImage.EMPTY)): ObserverFormFields()
    class BestBefore(val value: Long?): ObserverFormFields()
    object None: ObserverFormFields()
}
