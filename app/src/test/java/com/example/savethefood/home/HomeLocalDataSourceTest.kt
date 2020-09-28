package com.example.savethefood.home

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.savethefood.MainCoroutineRule
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.asDatabaseModel
import com.example.savethefood.data.source.local.dao.FoodDatabaseDao
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.viewmodel.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HomeLocalDataSourceTest {

    private lateinit var userDao: FoodDatabaseDao
    private lateinit var db: SaveTheFoodDatabase

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata
    //This swap the standard coroutine main dispatcher to the test dispatcher
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        //var databaseMock: SaveTheFoodDatabase
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            SaveTheFoodDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        userDao = db.foodDatabaseDao
    }

    @Test
    @Throws(Exception::class)
    fun writeNewFood_Successful() {
        //TODO Read mockito
        /*val food = Mockito.mock(FoodDomain::class.java)
        Mockito.verify(food)
        Mockito.`when`(food.foodId != 0).thenReturn(true)*/

        val insertId = userDao.insert(FoodDomain().asDatabaseModel())
        val records = userDao.getFoods().getOrAwaitValue()
        assert(records.any { it.id == insertId.toInt() })
    }

    fun test1() {
        // GIVEN
        // WHEN
        //THEN
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}