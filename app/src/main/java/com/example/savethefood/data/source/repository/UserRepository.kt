package com.example.savethefood.data.source.repository

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    /**
     * SAve locally for now, TODO save online, retrieve data and save locally
     */
    suspend fun saveNewUser(user: UserDomain)

    suspend fun getUser(user: UserDomain, ioDispatcher: CoroutineDispatcher = Dispatchers.IO): Result<UserDomain>
}