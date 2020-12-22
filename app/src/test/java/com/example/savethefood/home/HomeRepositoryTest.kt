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
import com.example.savethefood.data.succeeded
import com.example.savethefood.util.FoodImage
import com.example.savethefood.util.StorageType
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
import java.util.*

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

    // TODO Follow the test here and use any to set up the then return https://en.paradigmadigital.com/dev/android-testing-how-to-perform-unit-tests/
    @Before
    fun createRepository() = mainCoroutineRule.runBlockingTest{
        fakeRemoteDataSourceTest = mock(FakeRemoteFoodDataSourceTest::class.java)
        foodFromApi = Result.Success(FoodDomain("1","1",1, FoodImage.EMPTY, 2.0, 0.0, 1.0, StorageType.UNKNOWN, Date()))
        `when`(fakeRemoteDataSourceTest.getFoodById(1)).thenReturn(foodFromApi) // Set the mock to return the data from api when 1
        fakeLocalFoodDataSourceTest = mock(FakeLocalFoodDataSourceTest::class.java)
        foodFromDao = Result.Success((FoodDomain("2","2",1, FoodImage.EMPTY, 2.0, 0.0, 1.0, StorageType.UNKNOWN, Date())))
        `when`(fakeLocalFoodDataSourceTest.getFoodById(2)).thenReturn(foodFromDao) // Set the mock to return the data from dao when 1
        `when`(fakeLocalFoodDataSourceTest.getFoodById(0)).thenReturn(Result.Error("No Data")) // Set the mock to return no value found
        `when`(fakeRemoteDataSourceTest.getFoodById(0)).thenReturn(Result.Error("No Data")) // Set the mock to return no value found
        foodRepository = FakeFoodDataRepositoryTest(fakeRemoteDataSourceTest, fakeLocalFoodDataSourceTest)
    }

    @Test
    fun testGetFoodApi_success() = mainCoroutineRule.runBlockingTest{
        val food = foodRepository.getApiFoodById(1)
        assert(food.succeeded)
    }

    @Test
    fun testGetFoodApi_fail() = mainCoroutineRule.runBlockingTest{
        val food = foodRepository.getApiFoodById(0)
        assert(food is Result.Error)
    }

    @Test
    fun testGetFoodDao_success() = mainCoroutineRule.runBlockingTest{
        val food = foodRepository.getApiFoodById(2)
        assert(food.succeeded)
    }

    @Test
    fun testGetFoodDao_fail() = mainCoroutineRule.runBlockingTest{
        val food = foodRepository.getApiFoodById(0)
        assert(food is Result.Error)
    }
}