package com.example.savethefood.data.source.repository

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.domain.RecipeIngredients
import com.example.savethefood.data.source.RecipeDataSource
import com.example.savethefood.util.wrapEspressoIdlingResource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

class RecipeDataRepository @Inject constructor(
    @field:[Named("RecipeLocalDataSource")] private val recipeLocalDataSource: RecipeDataSource,
    @field:[Named("RecipeRemoteDataSource")] private val recipeRemoteDataSource: RecipeDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecipeRepository {

    @Throws(Exception::class)
    override fun getRecipes(): Flow<Result<RecipeDomain>> {
        return wrapEspressoIdlingResource {
            // TODO use CacheOnSuccess like advance coroutines with oneach and change from flow to suspend and coroutine
            recipeRemoteDataSource.getRecipes()
                .map {
                    if (it != null) {
                        Result.Success(it)
                    } else {
                        Result.Error("No data")
                    }
                }
                .retryWhen { cause, attempt ->
                    retryConnection(cause, attempt)
                }
                .flowOn(ioDispatcher)
        }
    }

    /**
     * Get the recipe with ingredients and combine them with the saved ones
     */
    override fun getRecipesByIngredients(vararg foodFilter: String?): Flow<Result<List<RecipeIngredients>?>> {
        return wrapEspressoIdlingResource {
            val remoteRecipes = recipeRemoteDataSource.getRecipesByIngredients(*foodFilter)
            recipeLocalDataSource.getRecipesIngredients()
                .retryWhen { cause, attempt ->
                    retryConnection(cause, attempt)
                }
                .combine(remoteRecipes) { local, remote ->
                    //local?.union(remote ?: listOf())
                    local?.toMutableList()?.applyRemoteRecipes(remote)
                }
                .map(::recipeIngredientResult)
        }
    }

    /**
     * Create the complete list mixing the local and remote
     */
    private fun MutableList<RecipeIngredients>.applyRemoteRecipes(
        remoteRecipes: List<RecipeIngredients>?
    ): List<RecipeIngredients> {
        val recipesToAdd =
            remoteRecipes?.filter { recipeIngredients -> this.none { local -> local.id == recipeIngredients.id } }
        this.addAll(recipesToAdd ?: listOf())
        return this
    }

    @Throws(Exception::class)
    override suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain> = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            recipeRemoteDataSource.getRecipeInfo(id)
        }
    }

    /**
     * Save the recipe or delete it using the flag of RecipeIngredients class
     */
    override suspend fun saveRecipe(recipe: RecipeIngredients) = withContext(ioDispatcher){
        wrapEspressoIdlingResource {
            try {
                if (recipe.saved) {
                    val deleteRecipe = recipeLocalDataSource.deleteRecipe(recipe)
                    recipe.saved = deleteRecipe != 0
                    Result.Success(recipe)
                } else {
                    recipe.saved = true
                    val newRecipe = recipeLocalDataSource.saveRecipe(recipe)
                    Result.Success(newRecipe)
                }
            } catch (e: Exception) {
                Result.ExError(e)
            }
        }
    }

    /**
     * Calculate the result and order the list based on the total ingredients matched
     */
    private fun recipeIngredientResult(list: List<RecipeIngredients>?): Result<List<RecipeIngredients>> {
        return list?.let {
            if (it.count() > 0) {
                Result.Success(it.sortedBy(RecipeIngredients::title))
            } else {
                Result.Error("No data")
            }
        } ?: Result.ExError(Exception("Error retrieving data"))
    }

    // TODO find a ext fun, it's used in other repos
    private suspend fun retryConnection(cause: Throwable, attempt: Long) =
        if (cause is IOException && attempt < 5) {    // retry on IOException
            delay(10000)                     // delay for one second before retry
            true
        } else {                                      // do not retry otherwise
            false
        }
}