package com.example.savethefood.local.domain

import android.os.Parcelable
import com.example.savethefood.local.entity.UserEntity
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
