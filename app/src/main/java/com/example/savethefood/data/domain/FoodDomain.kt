package com.example.savethefood.data.domain

import android.os.Parcelable
import com.example.savethefood.data.source.local.entity.FoodEntity
import com.example.savethefood.util.FoodImage
import com.example.savethefood.util.QuantityType
import com.example.savethefood.util.StorageType
import kotlinx.android.parcel.Parcelize
import java.time.Year
import java.util.*


//Food
@Parcelize
data class FoodDomain(
    var foodTitle: String = "",
    var foodDescription: String? = "",
    var foodId: Int,
    var foodImg: FoodImage,
    var price: Double?,
    var quantityType: QuantityType,
    var quantity: Double?,
    var storageType: StorageType,
    var bestBefore: Date
) : Parcelable {
    constructor() : this("", "",0, FoodImage.EMPTY,0.0, QuantityType.UNIT, 0.0, StorageType.UNKNOWN, Calendar.getInstance().time)
}

fun FoodDomain.asDatabaseModel(): FoodEntity {
    return FoodEntity(
        title = foodTitle,
        description = foodDescription,
        img = foodImg,
        price = price,
        quantityType = quantityType,
        quantity = quantity,
        storageType = storageType,
        foodBestBefore = bestBefore.time
    )
}
