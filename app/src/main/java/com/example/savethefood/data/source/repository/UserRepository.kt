package com.example.savethefood.data.source.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Room
import com.example.savethefood.data.Result
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.domain.asDatabaseModel
import com.example.savethefood.data.source.UserDataSource
import com.example.savethefood.data.source.local.datasource.UserLocalDataSource
import com.example.savethefood.data.source.local.entity.asDomainModel
import kotlinx.coroutines.*

class UserRepository(
    private val userLocalDataSource: UserDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getRepository(app: Application): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val database = SaveTheFoodDatabase.getInstance(app)
                return@synchronized UserRepository(
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
    suspend fun saveNewUser(user: UserDomain) {
        withContext(ioDispatcher) {
            userLocalDataSource.saveUser(user)
        }
        //We could use this one, but it's useful only for multiple job children
        /*coroutineScope {
            launch { userLocalDataSource.saveUser(user) }
        }*/
    }

    suspend fun getUser(user: UserDomain): Result<UserDomain?> {
        return userLocalDataSource.getUser(user.userEmail, user.userPassword)
    }
}