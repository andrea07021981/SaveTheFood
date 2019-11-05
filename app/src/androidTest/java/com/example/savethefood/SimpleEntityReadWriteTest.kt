package com.example.savethefood

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.example.savethefood.local.dao.UserDatabaseDao
import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.local.domain.User
import com.example.savethefood.local.entity.UserEntity
import com.example.savethefood.local.entity.asDomainModel
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

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