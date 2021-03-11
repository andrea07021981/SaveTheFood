package com.example.savethefood


import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.domain.asDatabaseModel
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
abstract class DbTest {
    protected lateinit var appDatabase: SaveTheFoodDatabase

    @Before
    fun initDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                SaveTheFoodDatabase::class.java)
                .build()
    }

    @Test
    fun insert_user_done() {
        val user = UserDomain().apply {
            userName = "Username"
            email = "Useremail"
            password = "password"
        }.asDatabaseModel()
        val newId = appDatabase.userDatabaseDao.insert(user)
        assertEquals(newId, 0)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }
}