package com.example.savethefood.data.source.local.datasource

import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.domain.asDatabaseModel
import com.example.savethefood.data.source.UserDataSource
import com.example.savethefood.data.source.local.entity.UserEntity
import java.lang.Exception
import kotlin.jvm.Throws

class FakeUserDataSourceTest(
    var users: MutableList<UserEntity>? = mutableListOf()
) : UserDataSource {

    //TODO add fake data, not related with local or remote

    @Throws(Exception::class)
    override fun getUser(email: String, password: String): UserEntity? {
        return users?.find { it.email == email && it.password == password }
    }

    override suspend fun saveUser(user: UserDomain): Long {
        users?.add(user.asDatabaseModel())
        return 1 // Default fake result
    }
}