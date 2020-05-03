package com.example.savethefood.home

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.viewmodel.getOrAwaitValue
import org.checkerframework.common.reflection.qual.GetClass
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setupViewModel() {
        homeViewModel = HomeViewModel(ApplicationProvider.getApplicationContext())
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
        homeViewModel.moveToBarcodeReader()

        //Then the new task event is triggered
        val value = homeViewModel.barcodeFoodEvent.getOrAwaitValue()
        assertThat(
            value.getContentIfNotHandled(), not(nullValue()))
    }
}