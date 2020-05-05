package com.example.savethefood.data.source.local.datasource

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.UserDataSource
import org.junit.Assert.*

class FakeUserDataSourceTest(var users: MutableList<UserDomain>? = mutableListOf()) : UserDataSource {

    //TODO add fake data, not related with local or remote

    override suspend fun getUser(email: String, password: String): Result<UserDomain> {
        val userFound = users?.find { it.userEmail == email && it.userPassword == password }
        if (userFound != null) {
            return Result.Success(userFound)
        }
        return Result.Error("Not found")
    }

    override suspend fun saveUser(user: UserDomain): Long {
        users?.add(user)
        return 1 // Default fake result
    }
}