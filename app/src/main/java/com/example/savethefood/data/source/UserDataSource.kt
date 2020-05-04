package com.example.savethefood.data.source

import androidx.lifecycle.LiveData
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain

interface UserDataSource {

    suspend fun getUser(email: String, password: String): Result<UserDomain>

    suspend fun saveUser(user: UserDomain)
}