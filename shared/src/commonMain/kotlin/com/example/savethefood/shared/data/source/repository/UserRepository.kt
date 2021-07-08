package com.example.savethefood.shared.data.source.repository

import com.example.savethefood.shared.data.ActionResult
import com.example.savethefood.shared.data.domain.UserDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    /**
     * SAve locally for now, TODO save online, retrieve data and save locally
     */
    suspend fun saveNewUser(user: UserDomain): Long

    suspend fun getUser(user: UserDomain, ioDispatcher: CoroutineDispatcher = Dispatchers.Default): ActionResult<UserDomain>
}