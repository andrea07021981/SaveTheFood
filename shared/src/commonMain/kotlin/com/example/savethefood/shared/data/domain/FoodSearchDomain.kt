package com.example.savethefood.shared.data.domain

import kotlinx.serialization.Serializable

@Serializable
data class FoodSearchDomain(
    var number: Int,
    var offset: Int,
    var products: List<ProductDomain>,
    var totalProducts: Int,
    var type: String
)