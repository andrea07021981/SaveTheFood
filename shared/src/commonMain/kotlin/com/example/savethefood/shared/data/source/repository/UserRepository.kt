package com.example.savethefood.shared.data.source.repository

import com.example.savethefood.shared.data.Result
import com.example.savethefood.shared.data.domain.UserDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface UserRepository {
    /**
     * SAve locally for now, TODO save online, retrieve data and save locally
     */
    suspend fun saveNewUser(user: UserDomain): Long

    suspend fun getUser(user: UserDomain, ioDispatcher: CoroutineDispatcher = Dispatchers.Default): Result<UserDomain>
}