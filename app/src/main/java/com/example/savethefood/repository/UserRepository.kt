package com.example.savethefood.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.local.domain.User
import com.example.savethefood.local.domain.asDatabaseModel
import com.example.savethefood.local.entity.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val database: SaveTheFoodDatabase) {

    suspend fun saveNewUser(user: User) {
        withContext(Dispatchers.IO) {
            val newId = database.userDatabaseDao.insert(user.asDatabaseModel())
        }
    }

    suspend fun getUser(user: User): LiveData<User?> {
        return withContext(Dispatchers.IO) {
            val userFound = database.userDatabaseDao.getUser(user.userEmail, user.userPassword)
            Transformations.map(userFound) {
                it?.asDomainModel()
            }
        }
    }
}