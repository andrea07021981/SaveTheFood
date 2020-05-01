package com.example.savethefood

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.savethefood.local.entity.UserEntity
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest : DbTest() {

    @Test
    fun insertAndRetrieveUserTest() {
        val user = UserEntity(userName = "Test User", email = "Test User", password = "Test User")
        val userId = appDatabase.userDatabaseDao.insert(user = user)
        val userFromDb = appDatabase.userDatabaseDao.getUser("Test User", "Test User")
        Assert.assertEquals(userId, 1)
        //Assert.assertEquals(userFromDb?.value?.userName, user.userName)
    }
}