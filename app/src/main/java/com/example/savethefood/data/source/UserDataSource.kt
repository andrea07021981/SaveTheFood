package com.example.savethefood.data.source

import androidx.lifecycle.LiveData
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Deprecated("Moved to kmm module")
interface UserDataSource {

    suspend fun getUser(email: String, password: String): UserEntity?

    suspend fun saveUser(user: UserDomain): Long
}