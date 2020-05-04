package com.example.savethefood.data.source.repository

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import org.junit.Assert.*

class FakeUserDataRepositoryTest : UserRepository {

    //Todo implements methods, then creare
    override suspend fun saveNewUser(user: UserDomain) {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(user: UserDomain): Result<UserDomain?> {
        TODO("Not yet implemented")
    }
}