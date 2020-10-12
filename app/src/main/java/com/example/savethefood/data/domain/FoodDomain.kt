package com.example.savethefood.data.domain

import android.os.Parcelable
import com.example.savethefood.data.source.local.entity.FoodEntity
import kotlinx.android.parcel.Parcelize
import java.util.*


//Food
@Parcelize
data class FoodDomain(
    var foodTitle: String = "",
    var foodDescription: String? = "",
    var foodId: Int,
    // used to map img_src from the JSON to imgSrcUrl in our class
    var foodImgUrl: String,
    var likes: Double?,
    var price: Double?,
    var calories: Double?,
    var fat: String?,
    var proteins: String?,
    var carbs: String?,
    var ingredientList: String?,
    var servingSize: String?,
    var bestBefore: Date

) : Parcelable {

    constructor() : this("", "",0,"", 0.0, 0.0, 0.0, "", "", "", "", "", Date())
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
        carbs = carbs,
        ingredientList = ingredientList,
        servingSize = servingSize,
        foodBestBefore = bestBefore.time
    )
}
