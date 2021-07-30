package com.example.savethefood.shared.data.source.repository

import com.example.savethefood.shared.data.Result
import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.data.source.UserDataSource
import com.example.savethefood.shared.data.source.local.entity.UserEntity
import com.example.savethefood.shared.data.source.local.entity.asDomainModel
import kotlinx.coroutines.CoroutineDispatcher


class UserDataRepository(
    private val userLocalDataSource: UserDataSource,
    private val userRemoteDataSource: UserDataSource,
) : UserRepository {

    /**
     * SAve locally for now, TODO save online, retrieve data and save locally Firebase
     */
    override suspend fun saveNewUser(user: UserDomain): Long {
        return userLocalDataSource.saveUser(user)
    }

    override suspend fun getUser(user: UserDomain, ioDispatcher: CoroutineDispatcher): Result<UserDomain> {
        return try {
            val userDb =
                userLocalDataSource.getUser(user.email, user.password)
                    ?: kotlin.run {
                        //CAll the API like firebase auth, no local user
                    }
            if (userDb is UserEntity) {
                Result.Success(userDb.asDomainModel())
            } else {
                Result.Error("User Not found")
            }
        } catch (e: Exception) {
            Result.ExError(e)
        }
    }
}