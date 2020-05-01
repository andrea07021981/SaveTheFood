package com.example.savethefood


import android.service.autofill.Validators.not
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.local.domain.UserDomain
import com.example.savethefood.local.domain.asDatabaseModel
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertNotEquals
import org.junit.Before
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

    fun insert_user_done() {
        val user = UserDomain().apply {
            userName = "Username"
            userEmail = "Useremail"
            userPassword = "password"
        }.asDatabaseModel()
        val newId = appDatabase.userDatabaseDao.insert(user)
        assertNotEquals(newId, 0)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }
}