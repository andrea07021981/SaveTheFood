package com.example.savethefood


import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.example.savethefood.local.database.SaveTheFoodDatabase
import org.junit.After
import org.junit.Before
import java.io.IOException

abstract class DbTest {
    protected lateinit var appDatabase: SaveTheFoodDatabase

    @Before
    fun initDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                SaveTheFoodDatabase::class.java)
                .build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }
}