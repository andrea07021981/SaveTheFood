package com.example.savethefood.recipe

import android.os.Build
import android.os.Looper
import android.util.SparseArray
import android.util.SparseIntArray
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.collection.arrayMapOf
import androidx.collection.arraySetOf
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.savethefood.MainCoroutineRule
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.local.datasource.FakeRecipeDataSourceTest
import com.example.savethefood.data.source.local.datasource.FakeUserDataSourceTest
import com.example.savethefood.data.source.repository.FakeRecipeDataRepositoryTest
import com.example.savethefood.data.source.repository.FakeUserDataRepositoryTest
import com.example.savethefood.viewmodel.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/* TODO move to instrumented test
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class RecipeViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata
    //This swap the standard coroutine main dispatcher to the test dispatcher
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var fakeRecipeDataRepositoryTest: FakeRecipeDataRepositoryTest

    @Before
    fun setupViewModel() {
        val recipeList = arrayListOf(
            RecipeResult(1, "w", "W", "W", 1, 1, "w"),
            RecipeResult(2, "w", "W", "W", 1, 1, "w"),
            RecipeResult(3, "w", "W", "W", 1, 1, "w"),
            RecipeResult(4, "w", "W", "W", 1, 1, "w"),
            RecipeResult(5, "w", "W", "W", 1, 1, "w"),
            RecipeResult(6, "w", "W", "W", 1, 1, "w")
        )
        fakeRecipeDataRepositoryTest = FakeRecipeDataRepositoryTest(FakeRecipeDataSourceTest(recipeList))
        recipeViewModel = RecipeViewModel(fakeRecipeDataRepositoryTest, "")
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
        // runBlockingTest gives you finer control over virtual time if you need it
        // runBlocking is good for testing non-delays
        mainCoroutineRule.launch(Dispatchers.Default) {
            recipeViewModel.searchFilter.value = ""
            //Given a list of results
            val flowRequest = fakeRecipeDataRepositoryTest.getRecipes("")
                .onStart {
                }
                .catch {
                }
                .transform { value ->
                    if (value is Result.Success) {
                        emit(value.data.results)
                    }
                }
                .onCompletion {
                }

            // WHEN you update the list,
            recipeViewModel._recipeListResult =
                flowRequest
                    .asLiveData()
            //Then the new task event is triggered and the array is not empty
            val orAwaitValue = recipeViewModel.recipeListResult.getOrAwaitValue()
            assertFalse(orAwaitValue.value != null)
        }
    }

    @After
    fun close() {

    }
}
*/
