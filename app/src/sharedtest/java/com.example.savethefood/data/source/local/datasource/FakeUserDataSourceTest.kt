package com.example.savethefood.data.source.local.datasource

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.UserDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*

class FakeUserDataSourceTest(
    var users: MutableList<UserDomain>? = mutableListOf()
) : UserDataSource {

    //TODO add fake data, not related with local or remote

    override suspend fun getUser(email: String, password: String): Flow<Result<UserDomain>>  = flow {
        val userFound = users?.find { it.userEmail == email && it.userPassword == password }
        if (userFound != null) {
            emit(Result.Success(userFound))
        }
        emit(Result.Error("Not found"))
    }

    override suspend fun saveUser(user: UserDomain): Long {
        users?.add(user)
        return 1 // Default fake result
    }
}