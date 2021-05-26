package com.example.savethefood.data.domain

import android.os.Parcelable
import com.example.savethefood.data.source.local.entity.UserEntity
import kotlinx.android.parcel.Parcelize

// TODO consider inline classes for properties
@Parcelize
data class UserDomain(
    var userName: String = "",
    var email: String = "",
    var password: String = ""
) : Parcelable {

    constructor() : this("", "","")
}

fun UserDomain.asDatabaseModel(): UserEntity {
    return UserEntity(
        userName = userName,
        email = email,
        password = password
    )
}