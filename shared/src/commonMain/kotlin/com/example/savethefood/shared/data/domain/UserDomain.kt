package com.example.savethefood.shared.data.domain

import com.example.savethefood.shared.Parcelable
import com.example.savethefood.shared.Parcelize
import com.example.savethefood.shared.data.source.local.entity.UserEntity
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserDomain(
    var userName: String = "",
    var email: String = "",
    var password: String = ""
) : Parcelable

fun UserDomain.asDatabaseModel(): UserEntity {
    return UserEntity(
        userName = userName,
        email = email,
        password = password
    )
}