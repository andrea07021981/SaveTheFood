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

class RecipeDataRepository @Inject constructor(
    private val recipeLocalDataSource: RecipeDataSource,
    private val recipeRemoteDataSource: RecipeDataSource,
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
                .retryWhen {cause, attempt ->
                    retryConnection(cause, attempt)
                }
                .flowOn(ioDispatcher)
        }
    }

    override fun getRecipesByIngredients(vararg foodFilter: String?): Flow<Result<List<RecipeIngredients>?>> {
        return wrapEspressoIdlingResource {
            val flowLocalRecipes = recipeLocalDataSource.getRecipesIngredients()
            recipeRemoteDataSource.getRecipesByIngredients(*foodFilter)
                .map(::recipeIngredientResult)
                .retryWhen { cause, attempt ->
                    retryConnection(cause, attempt)
                }
                .flowOn(ioDispatcher)
                .combine(flowLocalRecipes) { remote, local ->
                    if (!local.isNullOrEmpty()) {
                        local.forEach { localRecipe ->
                            if (remote is Result.Success) {
                                remote.data.map {
                                    it.saved = it.id == localRecipe.id
                                }
                            }
                        }

                    }
                    remote
                }
        }
    }

    @Throws(Exception::class)
    override suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain> = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            recipeRemoteDataSource.getRecipeInfo(id)
        }
    }

    // TODO review the multiple runs
    override suspend fun saveRecipe(recipe: RecipeIngredients) = withContext(ioDispatcher){
        wrapEspressoIdlingResource {
            val dbRecipe = recipeLocalDataSource.getRecipeIngredients(recipe.id)

            // If present, remove from favourites and return
            dbRecipe?.run {
                val deleteRecipe = recipeLocalDataSource.deleteRecipe(recipe)
                recipe.saved = deleteRecipe != 0
                Result.Success(recipe)
            } ?: run {
                val newRecipe = recipeLocalDataSource.saveRecipe(recipe)
                newRecipe?.run {
                    this.saved = true
                    Result.Success(this)
                } ?: Result.Error("No record inserted")
            }
        }
    }

    private fun recipeIngredientResult(list: List<RecipeIngredients>?): Result<List<RecipeIngredients>> {
        return list?.let {
            if (it.count() > 0) {
                Result.Success(it)
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