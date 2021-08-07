package com.example.savethefood.data.domain

import android.os.Parcelable
import com.example.savethefood.constants.QuantityType
import com.example.savethefood.data.source.local.entity.BagEntity
import com.example.savethefood.util.FoodImage
import kotlinx.android.parcel.Parcelize


//Food
@Parcelize
data class BagDomain(
    val id: Int,
    var title: String = "",
    var img: FoodImage,
    var quantityType: QuantityType?,
    var quantity: Double?,
) : Parcelable {
    constructor() : this(0, "", FoodImage.EMPTY, QuantityType.UNIT,0.0)
}

fun BagDomain.asDatabaseModel(): BagEntity {
    return BagEntity(
        id = id,
        title = title,
        img = img,
        quantityType = quantityType,
        quantity = quantity
    )
}