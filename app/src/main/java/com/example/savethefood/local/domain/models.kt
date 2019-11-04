package com.example.savethefood.local.domain

import com.example.savethefood.local.entity.UserEntity

//User
data class User(var userName: String = "",
                 var userEmail: String = "",
                 var userPassword: String = "") {

    constructor() : this("", "","")
}

fun User.asDatabaseModel(): UserEntity {
    return UserEntity(
            userName = userName,
            email = userEmail,
            password = userPassword)
}
