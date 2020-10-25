package com.example.savethefood.data.source.remote.datasource

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.UserDataSource
import com.example.savethefood.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

//TODO remote login
class UserRemoteDataSource : UserDataSource {
    override  fun getUser(email: String, password: String): UserEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(user: UserDomain): Long {
        TODO("Not yet implemented")
    }
}