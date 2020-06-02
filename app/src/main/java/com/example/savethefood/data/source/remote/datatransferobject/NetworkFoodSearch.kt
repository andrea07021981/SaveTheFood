package com.example.savethefood.data.source.remote.datatransferobject

import com.example.savethefood.data.domain.FoodSearchDomain
import com.example.savethefood.data.domain.ProductDomain
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkFoodSearch(
    val number: Int,
    val offset: Int,
    val products: List<NetworkProduct>,
    val totalProducts: Int,
    val type: String
)

@JsonClass(generateAdapter = true)
data class NetworkProduct(
    val id: Int,
    val image: String,
    val imageType: String,
    val title: String
)

/**
 * Convert Network results to database objects
 */
fun NetworkFoodSearch.asDomainModel() : FoodSearchDomain {
    return FoodSearchDomain(
        number = number,
        offset = offset,
        products = products.map {
            ProductDomain(
                it.id,
                it.image,
                it.imageType,
                it.title
            )
        },
        totalProducts = totalProducts,
        type = type
    )
}


//TODO ADD converter for local data