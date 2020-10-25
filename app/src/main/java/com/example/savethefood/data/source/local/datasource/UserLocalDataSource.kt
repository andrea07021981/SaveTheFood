package com.example.savethefood.data.source.local.datasource

import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.UserDataSource
import com.example.savethefood.data.source.local.dao.UserDatabaseDao
import com.example.savethefood.data.source.local.entity.asDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.asDatabaseModel
import com.example.savethefood.data.source.local.entity.UserEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlin.Exception
import kotlin.jvm.Throws

class UserLocalDataSource internal constructor(
    private val userDatabaseDao: UserDatabaseDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource {

    @Throws(Exception::class)
    override fun getUser(email: String, password: String): UserEntity? {
        return userDatabaseDao.getUser(email, password)
    }

    override suspend fun saveUser(user: UserDomain) = withContext(ioDispatcher){
        userDatabaseDao.insert(user.asDatabaseModel())
    }
}