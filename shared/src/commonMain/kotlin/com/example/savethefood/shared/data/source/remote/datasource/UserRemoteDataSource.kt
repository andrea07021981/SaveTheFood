package com.example.savethefood.shared.data.source.remote.datasource

import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.data.source.UserDataSource
import com.example.savethefood.shared.data.source.local.entity.UserEntity


//TODO remote login
class UserRemoteDataSource(

): UserDataSource {
    override suspend fun getUser(email: String, password: String): UserDomain {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(user: UserDomain): Long {
        TODO("Not yet implemented")
    }
}
