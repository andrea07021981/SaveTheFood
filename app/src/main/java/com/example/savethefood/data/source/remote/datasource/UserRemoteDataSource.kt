package com.example.savethefood.data.source.remote.datasource

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.UserDataSource

//TODO remote login
class UserRemoteDataSource : UserDataSource {
    override suspend fun getUser(email: String, password: String): Result<UserDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(user: UserDomain): Long {
        TODO("Not yet implemented")
    }
}