package com.example.savethefood.data.source.repository

import androidx.annotation.VisibleForTesting
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.UserDataSource
import com.example.savethefood.data.source.local.datasource.FakeUserDataSourceTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.jetbrains.annotations.TestOnly
import org.junit.Assert.*

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

    override suspend fun getUser(user: UserDomain, ioDispatcher: CoroutineDispatcher): Flow<Result<UserDomain>> {
        return fakeUserDataRepositoryTest.getUser(user.userEmail, user.userPassword)
    }

    @VisibleForTesting
    suspend fun addUsers(vararg users: UserDomain) {
        for (user in users) {
            fakeUserDataRepositoryTest.saveUser(user)
        }
    }
}