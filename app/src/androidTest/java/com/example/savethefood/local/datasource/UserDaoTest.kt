package com.example.savethefood.local.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.domain.asDatabaseModel
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest //Because is a unit test with a small portion of functionalites
class UserDaoTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: SaveTheFoodDatabase

    @Before
    fun initDb() {
        //inMemoryDatabaseBuilder create a temp db for test and destroy it immediately after the test is done
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SaveTheFoodDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    suspend fun insertUser_andRetrieveit() {
        // GIVEN a new user and insert it
        val user = UserDomain("a", "a@a", "a")
        val insertId = database.userDatabaseDao.insert(user.asDatabaseModel())

        // WHEN get the user from db
        val loaded = database.userDatabaseDao.getUserWithId(insertId)

        //THEN the loaded data contains the expected values
        assertThat(user.userName, `is`(loaded?.userName))
        assertThat(user.email, `is`(loaded?.email))
        assertThat(user.password, `is`(loaded?.password))
    }
}