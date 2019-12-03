package com.example.savethefood.local.domain

import android.os.Parcelable
import com.example.savethefood.local.entity.FoodEntity
import com.example.savethefood.local.entity.UserEntity
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize



//Food
@Parcelize
data class FoodDomain(
    var foodTitle: String = "",
    var foodDescription: String = "",
    var foodId: Int,
    // used to map img_src from the JSON to imgSrcUrl in our class
    var foodImgUrl: String,
    var likes: Double?,
    var price: Double?,
    var calories: Double?,
    var fat: String?,
    var proteins: String?,
    var carbs: String?
) : Parcelable {

    constructor() : this("", "",0,"", 0.0, 0.0, 0.0, "", "", "")
}

fun FoodDomain.asDatabaseModel(): FoodEntity {
    return FoodEntity(
        id = foodId,
        title = foodTitle,
        description = foodDescription,
        imgUrl = foodImgUrl,
        likes = likes,
        price = price,
        calories = calories,
        fat = fat,
        proteins = proteins,
        carbs = carbs
    )
}
