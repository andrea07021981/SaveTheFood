package com.example.savethefood.data.domain

import android.os.Parcelable
import com.example.savethefood.data.source.local.entity.FoodEntity
import com.example.savethefood.util.FoodImage
import com.example.savethefood.util.StorageType
import kotlinx.android.parcel.Parcelize
import java.util.*


//Food
@Parcelize
data class FoodDomain(
    var foodTitle: String = "",
    var foodDescription: String? = "",
    var foodId: Int,
    var foodImg: FoodImage,
    var likes: Double?,
    var price: Double?,
    var quantity: Double?,
    var storageType: StorageType,
    var bestBefore: Date
) : Parcelable {
    constructor() : this("", "",0, FoodImage.EMPTY, 0.0, 0.0, 0.0, StorageType.UNKNOWN, Date())
}

fun FoodDomain.asDatabaseModel(): FoodEntity {
    return FoodEntity(
        title = foodTitle,
        description = foodDescription,
        img = foodImg,
        likes = likes,
        price = price,
        quantity = quantity,
        storageType = storageType,
        foodBestBefore = bestBefore.time
    )
}
