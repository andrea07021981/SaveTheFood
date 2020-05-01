package com.example.savethefood.data.domain

import android.os.Parcelable
import com.example.savethefood.data.source.local.entity.UserEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDomain(
    var userName: String = "",
    var userEmail: String = "",
    var userPassword: String = ""
) : Parcelable {

    constructor() : this("", "","")
}

fun UserDomain.asDatabaseModel(): UserEntity {
    return UserEntity(
        userName = userName,
        email = userEmail,
        password = userPassword
    )
}