package com.example.savethefood.data.domain

import com.example.savethefood.data.source.local.entity.UserEntity
import kotlinx.serialization.Serializable

@Serializable
data class UserDomain(
    var userName: String = "",
    var email: String = "",
    var password: String = ""
)

fun UserDomain.asDatabaseModel(): UserEntity {
    return UserEntity(
        userName = userName,
        email = email,
        password = password
    )
}