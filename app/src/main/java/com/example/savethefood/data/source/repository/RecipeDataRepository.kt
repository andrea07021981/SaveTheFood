package com.example.savethefood.data.source.repository

import android.util.Log
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.domain.RecipeIngredients
import com.example.savethefood.data.domain.RecipeResult
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
    override fun getRecipes(): Flow<Result<List<RecipeResult>?>> {
        return wrapEspressoIdlingResource {
            val localRecipes = recipeLocalDataSource.getRecipes()
            recipeRemoteDataSource.getRecipes()
                .combine(localRecipes) { remote, local ->
                    //local?.union(remote ?: listOf())
                    remote?.results?.toMutableList().applyRemoteResultRecipes(local)
                }
                .map {
                    if (it.isNullOrEmpty().not()) {
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
                .flowOn(Dispatchers.Default)
                .conflate()
        }
    }

    /**
     * Create the complete list mixing the local and remote recipes for the recipe view
     */
    private fun MutableList<RecipeResult>?.applyRemoteResultRecipes(
        localRecipes: RecipeDomain?
    ): MutableList<RecipeResult>? {
        // We need to elaborate and compute, run works perfectly here
        this?.addAll(localRecipes?.results?.toMutableList() ?: listOf())
        return this
    }

    /**
     * Create the complete list mixing the local and remote
     */
    private fun MutableList<RecipeIngredients>.applyRemoteRecipes(
        remoteRecipes: List<RecipeIngredients>?
    ): List<RecipeIngredients> {
        val recipesToAdd =
            remoteRecipes
                ?.filter {
                    recipeIngredients -> this.none { local -> local.id == recipeIngredients.id }
                }?.map {
                    it.recipeId = 0L
                    it
                }
        // TODO not working
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
     * // TODO Spoonacular does not have a search recipe with id, call it with id and number = 1
     * // TODO so we can save a reciperesult domain to be used in recipe local view
     */
    override suspend fun saveRecipe(recipe: RecipeIngredients) = withContext(ioDispatcher){
        wrapEspressoIdlingResource {
            try {
                if (recipe.recipeId > 0L) {
                    val deleteRecipe = recipeLocalDataSource.deleteRecipe(recipe)
                    Result.Success(recipe)
                } else {
                    // TODO call the search recipe and save the reciperesult domain as well as RecipeIngredients
                        // Use the coroutine sequential by default to retrieve the remote first, then save RecipeResult
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
                // TODO replace with generic list ext func
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