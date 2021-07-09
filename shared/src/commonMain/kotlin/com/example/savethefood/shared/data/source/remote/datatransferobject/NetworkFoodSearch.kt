package com.example.savethefood.shared.data.source.remote.datatransferobject

import com.example.savethefood.shared.data.domain.FoodSearchDomain
import com.example.savethefood.shared.data.domain.ProductDomain
import kotlinx.serialization.Serializable

@Serializable
data class NetworkFoodSearch(
    val number: Int,
    val offset: Int,
    val products: List<NetworkProduct>,
    val totalProducts: Int,
    val type: String
)

@Serializable
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