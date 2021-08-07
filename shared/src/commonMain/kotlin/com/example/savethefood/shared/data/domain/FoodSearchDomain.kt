package com.example.savethefood.shared.data.domain

import com.example.savethefood.shared.Parcelable
import com.example.savethefood.shared.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class FoodSearchDomain(
    var number: Int,
    var offset: Int,
    var products: List<ProductDomain>,
    var totalProducts: Int,
    var type: String
) : Parcelable