package com.example.savethefood.data.domain

import android.os.Parcelable
import com.example.savethefood.data.source.local.entity.FoodEntity
import com.example.savethefood.util.FoodImage
import com.example.savethefood.constants.QuantityType
import com.example.savethefood.constants.StorageType
import com.example.savethefood.data.source.local.entity.BagEntity
import kotlinx.android.parcel.Parcelize
import java.util.*


//Food
@Parcelize
data class BagDomain(
    val id: Int,
    var title: String = "",
    var quantity: Double,
) : Parcelable {
    constructor() : this(0, "",0.0)
}

fun BagDomain.asDatabaseModel(): BagEntity {
    return BagEntity(
        id = id,
        title = title,
        quantity = quantity
    )
}