package com.example.savethefood.shared.data.domain

import com.example.savethefood.shared.utils.FoodImage
import kotlinx.serialization.Serializable

@Serializable
data class FoodItem(
    val name: String,
    val img: FoodImage
)