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
import javax.inject.Inject
import kotlin.Exception
import kotlin.jvm.Throws

class UserLocalDataSource @Inject constructor(
    private val userDatabaseDao: UserDatabaseDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource {

    @Throws(Exception::class)
    override suspend fun getUser(email: String, password: String) = withContext(ioDispatcher){
        userDatabaseDao.getUser(email, password)
    }

    override suspend fun saveUser(user: UserDomain) = withContext(ioDispatcher){
        return@withContext userDatabaseDao.insert(user.asDatabaseModel())
    }
}