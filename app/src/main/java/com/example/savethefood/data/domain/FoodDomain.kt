package com.example.savethefood.data.domain

import android.os.Parcelable
import com.example.savethefood.data.source.local.entity.FoodEntity
import com.example.savethefood.util.FoodImage
import com.example.savethefood.util.QuantityType
import com.example.savethefood.util.StorageType
import com.example.savethefood.util.isValidDouble
import kotlinx.android.parcel.Parcelize
import java.lang.StringBuilder
import java.util.*


//Food
@Parcelize
data class FoodDomain(
    var title: String = "",
    var description: String? = "",
    var id: Int,
    var img: FoodImage,
    var price: Double?,
    var quantityType: QuantityType,
    var quantity: Double?,
    var storageType: StorageType,
    var bestBefore: Date
) : Parcelable {
    constructor() : this("", "",0, FoodImage.EMPTY,null, QuantityType.UNIT, null, StorageType.FRIDGE, Calendar.getInstance().time)
}

fun FoodDomain.asDatabaseModel(): FoodEntity {
    return FoodEntity(
        title = title,
        description = description,
        img = img,
        price = price,
        quantityType = quantityType,
        quantity = quantity,
        storageType = storageType,
        foodBestBefore = bestBefore.time
    )
}
