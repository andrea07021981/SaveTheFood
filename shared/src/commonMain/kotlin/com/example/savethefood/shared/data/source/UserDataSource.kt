package com.example.savethefood.shared.data.source

import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.data.source.local.entity.UserEntity

interface UserDataSource {

    suspend fun getUser(email: String, password: String): UserEntity?

    suspend fun saveUser(user: UserDomain): Long
}