package com.example.savethefood.shared.data.source

import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.data.source.local.entity.UserEntity

interface UserDataSource {

    val tag: String
        get() = UserDataSource::class.simpleName!!

    suspend fun getUser(email: String, password: String): UserDomain?

    suspend fun saveUser(user: UserDomain): Long
}