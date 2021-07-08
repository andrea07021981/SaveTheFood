package com.example.savethefood.shared.data.domain

import com.example.savethefood.shared.data.source.local.entity.BagEntity
import com.example.savethefood.shared.utils.FoodImage
import com.example.savethefood.shared.utils.QuantityType
import kotlinx.serialization.Serializable

//Food
@Serializable
data class BagDomain(
    val id: Int,
    var title: String = "",
    var img: FoodImage,
    var quantityType: QuantityType,
    var quantity: Double?,
)

fun BagDomain.asDatabaseModel(): BagEntity {
    return BagEntity(
        id = id,
        title = title,
        img = img,
        quantityType = quantityType,
        quantity = quantity
    )
}