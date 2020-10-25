package com.example.savethefood.data.source

import androidx.lifecycle.LiveData
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserDataSource {

    fun getUser(email: String, password: String): UserEntity?

    suspend fun saveUser(user: UserDomain): Long
}