package com.example.savethefood.data.source.repository

import androidx.annotation.VisibleForTesting
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.UserDataSource
import com.example.savethefood.data.source.local.datasource.FakeUserDataSourceTest
import org.jetbrains.annotations.TestOnly
import org.junit.Assert.*

class FakeUserDataRepositoryTest(
    private val fakeUserDataRepositoryTest: FakeUserDataSourceTest
) : UserRepository {

    //Variable and fun for testing fake errors
    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun saveNewUser(user: UserDomain) {
        fakeUserDataRepositoryTest.saveUser(user)
    }

    override suspend fun getUser(user: UserDomain): Result<UserDomain> {
        return fakeUserDataRepositoryTest.getUser(user.userEmail, user.userPassword)
    }

    @VisibleForTesting
    suspend fun addUsers(vararg users: UserDomain) {
        for (user in users) {
            fakeUserDataRepositoryTest.saveUser(user)
        }
    }
}