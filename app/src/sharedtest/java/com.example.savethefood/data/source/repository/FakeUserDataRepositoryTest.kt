package com.example.savethefood.data.source.repository

import androidx.annotation.VisibleForTesting
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.local.datasource.FakeUserDataSourceTest
import com.example.savethefood.data.source.local.entity.asDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
class FakeUserDataRepositoryTest(
    private val fakeUserDataRepositoryTest: FakeUserDataSourceTest
) : UserRepository {

    val testDispatcher = TestCoroutineDispatcher()

    //Variable and fun for testing fake errors
    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun saveNewUser(user: UserDomain) {
        fakeUserDataRepositoryTest.saveUser(user)
    }

    override suspend fun getUser(user: UserDomain, ioDispatcher: CoroutineDispatcher): Result<UserDomain> {
        val userFound = fakeUserDataRepositoryTest.getUser(user.email, user.password)
        return if (userFound != null) {
            Result.Success(userFound.asDomainModel())
        } else {
            Result.Error("Not found")
        }
    }

    @VisibleForTesting
    suspend fun addUsers(vararg users: UserDomain) {
        for (user in users) {
            fakeUserDataRepositoryTest.saveUser(user)
        }
    }
}