package com.example.savethefood.home

import android.os.Looper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.savethefood.MainCoroutineRule
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.FoodDataSource
import com.example.savethefood.data.source.local.datasource.FakeLocalFoodDataSourceTest
import com.example.savethefood.data.source.local.datasource.FakeRemoteFoodDataSourceTest
import com.example.savethefood.data.source.repository.FakeFoodDataRepositoryTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.LooperMode

@ExperimentalCoroutinesApi
class HomeRepositoryTest {

    @Mock
    private lateinit var fakeRemoteDataSourceTest: FoodDataSource
    @Mock
    private lateinit var fakeLocalFoodDataSourceTest: FoodDataSource

    //Utilities
    private lateinit var foodFromApi: Result<FoodDomain>
    private lateinit var foodFromDao: Result<FoodDomain>

    private lateinit var foodRepository: FakeFoodDataRepositoryTest

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata
    //This swap the standard coroutine main dispatcher to the test dispatcher
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() = mainCoroutineRule.runBlockingTest{
        fakeRemoteDataSourceTest = mock(FakeRemoteFoodDataSourceTest::class.java)
        foodFromApi = Result.Success(FoodDomain("1", "1",1,"", 0.0, 0.0, 0.0, "", "", "", "", ""))
        `when`(fakeRemoteDataSourceTest.getFoodById(1)).thenReturn(foodFromApi) // Set the mock to return the data from api when 1
        fakeLocalFoodDataSourceTest = mock(FakeLocalFoodDataSourceTest::class.java)
        foodFromDao = Result.Success((FoodDomain("2", "2",2,"", 0.0, 0.0, 0.0, "", "", "", "", "")))
        `when`(fakeLocalFoodDataSourceTest.getFoodById(2)).thenReturn(foodFromDao) // Set the mock to return the data from dao when 1
        `when`(fakeLocalFoodDataSourceTest.getFoodById(0)).thenReturn(Result.Error("No Data")) // Set the mock to return no value found
        `when`(fakeRemoteDataSourceTest.getFoodById(0)).thenReturn(Result.Error("No Data")) // Set the mock to return no value found
        foodRepository = FakeFoodDataRepositoryTest(fakeRemoteDataSourceTest, fakeLocalFoodDataSourceTest)
    }

    @Test
    fun testGetFoodApi_success() = mainCoroutineRule.runBlockingTest{
        val food = foodRepository.getApiFoodById(1)
        assert(food is Result.Success)
    }

    @Test
    fun testGetFoodApi_fail() = mainCoroutineRule.runBlockingTest{
        val food = foodRepository.getApiFoodById(0)
        assert(food is Result.Error)
    }

    @Test
    fun testGetFoodDao_success() = mainCoroutineRule.runBlockingTest{
        val food = foodRepository.getApiFoodById(2)
        assert(food is Result.Success)
    }

    @Test
    fun testGetFoodDao_fail() = mainCoroutineRule.runBlockingTest{
        val food = foodRepository.getApiFoodById(0)
        assert(food is Result.Error)
    }
}