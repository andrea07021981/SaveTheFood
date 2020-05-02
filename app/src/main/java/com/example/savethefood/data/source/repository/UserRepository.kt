package com.example.savethefood.data.source.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.domain.asDatabaseModel
import com.example.savethefood.data.source.local.entity.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val database: SaveTheFoodDatabase
) {

    suspend fun saveNewUser(user: UserDomain) {
        withContext(Dispatchers.IO) {
            val newId = database.userDatabaseDao.insert(user.asDatabaseModel())
        }
    }

    suspend fun getUser(user: UserDomain): LiveData<UserDomain?> {
        return withContext(Dispatchers.IO) {
            val userFound = database.userDatabaseDao.getUser(user.userEmail, user.userPassword)
            return@withContext Transformations.map(userFound) {
                it?.asDomainModel()
            }
        }
    }
}