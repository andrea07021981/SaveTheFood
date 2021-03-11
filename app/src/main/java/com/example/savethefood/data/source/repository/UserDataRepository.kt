package com.example.savethefood.data.source.repository

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.UserDataSource
import com.example.savethefood.data.source.local.entity.UserEntity
import com.example.savethefood.data.source.local.entity.asDomainModel
import com.example.savethefood.util.wrapEspressoIdlingResource
import kotlinx.coroutines.*
import javax.inject.Inject

class UserDataRepository @Inject constructor(
    private val userLocalDataSource: UserDataSource,
    private val userRemoteDataSource: UserDataSource,
) : UserRepository {

    /**
     * SAve locally for now, TODO save online, retrieve data and save locally Firebase
     */
    override suspend fun saveNewUser(user: UserDomain) {
        wrapEspressoIdlingResource {
            userLocalDataSource.saveUser(user)
        }
    }

    override suspend fun getUser(user: UserDomain, ioDispatcher: CoroutineDispatcher): Result<UserDomain> {
        wrapEspressoIdlingResource {
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
}