package com.example.savethefood.shared.data.source.local.entity

import com.example.savethefood.shared.data.domain.UserDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    @SerialName("id")
    var id: Long = 0L,

    @SerialName("userName")
    val userName: String,

    @SerialName("email")
    var email: String,

    @SerialName("password")
    var password: String
)

fun UserEntity.asDomainModel(): UserDomain {
    return UserDomain(
        userName = userName,
        email = email,
        password = password
    )
}
