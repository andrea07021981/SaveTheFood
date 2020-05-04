package com.example.savethefood.recipedetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.viewmodel.getOrAwaitValue
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeDetailViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata

    private lateinit var recipeDetailViewModel: RecipeDetailViewModel

    @Before
    fun setupViewModel() {
        recipeDetailViewModel = RecipeDetailViewModel(ApplicationProvider.getApplicationContext(), RecipeResult())
    }

    @Test
    fun moveToRecipeSearch_recipeSearchEvent() {
        //When adding a new task
        //recipeDetailViewModel.moveToCookDetail(RecipeInfoDomain()) //TODO create default constructor with values

        //Then the new task event is triggered
        val value = recipeDetailViewModel.recipeCookingtEvent.getOrAwaitValue()
        assertThat(
            value.getContentIfNotHandled(), not(nullValue()))
    }
}