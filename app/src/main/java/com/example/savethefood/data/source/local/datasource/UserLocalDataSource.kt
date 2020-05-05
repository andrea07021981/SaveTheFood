package com.example.savethefood.data.source.local.datasource

import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.UserDataSource
import com.example.savethefood.data.source.local.dao.UserDatabaseDao
import com.example.savethefood.data.source.local.entity.asDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.asDatabaseModel
import kotlinx.coroutines.withContext

class UserLocalDataSource internal constructor(
    private val userDatabaseDao: UserDatabaseDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource {

    override suspend fun getUser(email: String, password: String): Result<UserDomain> = withContext(ioDispatcher){
        val user = userDatabaseDao.getUser(email, password)
        if (user != null) {
            return@withContext Result.Success(user.asDomainModel())
        } else {
            return@withContext Result.Error("User not found")
        }

    }

    override suspend fun saveUser(user: UserDomain) = withContext(ioDispatcher){
        userDatabaseDao.insert(user.asDatabaseModel())
    }
}