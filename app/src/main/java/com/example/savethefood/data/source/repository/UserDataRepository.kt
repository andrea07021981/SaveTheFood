package com.example.savethefood.data.source.repository

import android.app.Application
import com.example.savethefood.data.Result
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.UserDataSource
import com.example.savethefood.data.source.local.datasource.UserLocalDataSource
import kotlinx.coroutines.*

class UserDataRepository(
    private val userLocalDataSource: UserDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    companion object {
        @Volatile
        private var INSTANCE: UserDataRepository? = null

        fun getRepository(app: Application): UserDataRepository {
            return INSTANCE ?: synchronized(this) {
                val database = SaveTheFoodDatabase.getInstance(app)
                return@synchronized UserDataRepository(
                    UserLocalDataSource(database.userDatabaseDao)
                ).also {
                    INSTANCE = it
                }
            }
        }
    }

    /**
     * SAve locally for now, TODO save online, retrieve data and save locally
     */
    override suspend fun saveNewUser(user: UserDomain) {
        withContext(ioDispatcher) {
            userLocalDataSource.saveUser(user)
        }
        //We could use this one, but it's useful only for multiple job children
        /*coroutineScope {
            launch { userLocalDataSource.saveUser(user) }
        }*/
    }

    override suspend fun getUser(user: UserDomain): Result<UserDomain> {
        return userLocalDataSource.getUser(user.userEmail, user.userPassword)
    }
}