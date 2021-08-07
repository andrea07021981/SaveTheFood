package com.example.savethefood.shared.data.domain

import com.example.savethefood.shared.Parcelable
import com.example.savethefood.shared.Parcelize
import com.example.savethefood.shared.utils.FoodImage
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class FoodItem(
    val name: String,
    val img: FoodImage
) : Parcelable {
    constructor(foodImage: FoodImage) : this (foodImage.name, foodImage)
}