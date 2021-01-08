package com.example.savethefood.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.local.datasource.FakeLocalFoodDataSourceTest
import com.example.savethefood.data.source.local.datasource.FakeRemoteFoodDataSourceTest
import com.example.savethefood.data.source.repository.FakeFoodDataRepositoryTest
import com.example.savethefood.viewmodel.getOrAwaitValue
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/* TODO move to instrumented
@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata

    private lateinit var fakeFoodDataRepositoryTest: FakeFoodDataRepositoryTest
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setupViewModel() {
        fakeFoodDataRepositoryTest = FakeFoodDataRepositoryTest(FakeRemoteFoodDataSourceTest(), FakeLocalFoodDataSourceTest())
        homeViewModel = HomeViewModel(fakeFoodDataRepositoryTest)
    }

    @Test
    fun moveToFoodDetail_detailFoodEvent() {

        //When adding a new task
        homeViewModel.moveToFoodDetail(FoodDomain())

        //Then the new task event is triggered
        val value = homeViewModel.detailFoodEvent.getOrAwaitValue()
        assertThat(
            value.getContentIfNotHandled(), not(nullValue()))
    }

    @Test
    fun moveToBarcodeReader_barcodeReaderEvent() {

        //When adding a new task
        homeViewModel.navigateToBarcodeReader()

        //Then the new task event is triggered
        val value = homeViewModel.barcodeFoodEvent.getOrAwaitValue()
        assertThat(
            value.getContentIfNotHandled(), not(nullValue()))
    }
}

 */