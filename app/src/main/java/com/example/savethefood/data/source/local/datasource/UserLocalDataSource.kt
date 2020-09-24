package com.example.savethefood.data.source.local.datasource

import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.UserDataSource
import com.example.savethefood.data.source.local.dao.UserDatabaseDao
import com.example.savethefood.data.source.local.entity.asDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.asDatabaseModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.lang.Exception

class UserLocalDataSource internal constructor(
    private val userDatabaseDao: UserDatabaseDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource {

    override suspend fun getUser(email: String, password: String): Flow<Result<UserDomain>> = flow {
        try {
            emit(Result.Loading)
            //TODO remove it, test latency
            delay(2000)
            userDatabaseDao.getUser(email, password)
                .collect {
                    if (it != null) {
                        emit(Result.Success(it.asDomainModel()))
                    } else {
                        emit(Result.Error("User not found"))
                    }
                }
        } catch (e: Exception) {
            emit(Result.ExError(e))
        }
    }

    override suspend fun saveUser(user: UserDomain) = withContext(ioDispatcher){
        userDatabaseDao.insert(user.asDatabaseModel())
    }
}