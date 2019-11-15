package com.example.savethefood.local.domain

import android.os.Parcelable
import com.example.savethefood.local.entity.FoodEntity
import com.example.savethefood.local.entity.UserEntity
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

//User
@Parcelize
data class User(var userName: String = "",
                 var userEmail: String = "",
                 var userPassword: String = "") : Parcelable {

    constructor() : this("", "","")
}

fun User.asDatabaseModel(): UserEntity {
    return UserEntity(
            userName = userName,
            email = userEmail,
            password = userPassword)
}

//Food
@Parcelize
data class Food(var foodName: String = "",
                var foodId: Int,
                // used to map img_src from the JSON to imgSrcUrl in our class
                var foodImgUrl: String): Parcelable {

    constructor() : this("", 0,"")
}

fun Food.asDatabaseModel(): FoodEntity {
    return FoodEntity(
        id = foodId,
        name = foodName,
        imgUrl = foodImgUrl
    )
}
