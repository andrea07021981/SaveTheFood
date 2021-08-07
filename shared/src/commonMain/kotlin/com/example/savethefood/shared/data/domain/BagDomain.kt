package com.example.savethefood.shared.data.domain

import com.example.savethefood.shared.Parcelable
import com.example.savethefood.shared.Parcelize
import com.example.savethefood.shared.data.source.local.entity.BagEntity
import com.example.savethefood.shared.utils.FoodImage
import com.example.savethefood.shared.utils.QuantityType
import kotlinx.serialization.Serializable

//Food
@Serializable
@Parcelize
data class BagDomain(
    val id: Int = 0,
    var title: String = "",
    var img: FoodImage = FoodImage.EMPTY,
    var quantityType: QuantityType = QuantityType.UNIT,
    var quantity: Double? = 0.0,
) : Parcelable

fun BagDomain.asDatabaseModel(): BagEntity {
    return BagEntity(
        id = id,
        title = title,
        img = img,
        quantityType = quantityType,
        quantity = quantity
    )
}