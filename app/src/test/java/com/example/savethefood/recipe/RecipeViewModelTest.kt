package com.example.savethefood.recipe

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.viewmodel.getOrAwaitValue
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata

    private lateinit var recipeViewModel: RecipeViewModel

    @Before
    fun setupViewModel() {
        recipeViewModel = RecipeViewModel(ApplicationProvider.getApplicationContext(), "")
    }

    @Test
    fun moveToRecipeDetail_detailRecipeEvent() {
        //When adding a new task
        recipeViewModel.moveToRecipeDetail(RecipeResult())

        //Then the new task event is triggered
        val value = recipeViewModel.recipeDetailEvent.getOrAwaitValue()
        assertThat(
            value.getContentIfNotHandled(), not(nullValue()))
    }

    @Test
    fun updateDataList_listOfRecipes_returnEvent() {
        //Given a list of results
        val recipeList = arrayListOf<RecipeResult?>(
            RecipeResult(1, "w", "W", "W", 1, 1, "w"),
            RecipeResult(2, "w", "W", "W", 1, 1, "w"),
            RecipeResult(3, "w", "W", "W", 1, 1, "w"),
            RecipeResult(4, "w", "W", "W", 1, 1, "w"),
            RecipeResult(5, "w", "W", "W", 1, 1, "w"),
            RecipeResult(6, "w", "W", "W", 1, 1, "w")
        )

        // WHEN you update the list
        recipeViewModel.updateDataList(recipeList)

        //Then the new task event is triggered and the array is not empty
        val value = recipeViewModel.recipeListResult.getOrAwaitValue()
        assertFalse(value.isEmpty())
    }
}