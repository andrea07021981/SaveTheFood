package com.example.savethefood.shared.data.domain

import com.example.savethefood.shared.Parcelable
import com.example.savethefood.shared.Parcelize
import com.example.savethefood.shared.utils.FoodImage
import kotlinx.serialization.Serializable

// TODO add images for categories
@Serializable
@Parcelize
data class CategoryItem(
    val name: String
) : Parcelable {

}