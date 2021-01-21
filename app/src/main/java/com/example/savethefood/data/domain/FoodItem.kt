package com.example.savethefood.data.domain

import android.os.Parcelable
import com.example.savethefood.util.FoodImage
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FoodItem(
    val name: String,
    val img: FoodImage
) : Parcelable {
    constructor(foodImage: FoodImage) : this (foodImage.name, foodImage)
}
