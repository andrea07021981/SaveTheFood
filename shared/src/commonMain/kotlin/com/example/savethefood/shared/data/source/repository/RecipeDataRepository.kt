package com.example.savethefood.shared.data.source.repository

import com.example.savethefood.shared.data.Result
import com.example.savethefood.shared.data.domain.RecipeDomain
import com.example.savethefood.shared.data.domain.RecipeInfoDomain
import com.example.savethefood.shared.data.domain.RecipeIngredients
import com.example.savethefood.shared.data.domain.RecipeResult
import com.example.savethefood.shared.data.source.RecipeDataSource
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext


class RecipeDataRepository(
    private val recipeLocalDataSource: RecipeDataSource,
    private val recipeRemoteDataSource: RecipeDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : RecipeRepository {

    @Throws(Exception::class)
    override fun getRecipes(): Flow<Result<List<RecipeResult>?>> {
        // TODO add a cache system in memory if an exception occurs
        val localRecipes = recipeLocalDataSource.getRecipes()
        return recipeRemoteDataSource.getRecipes()
            .combine(localRecipes) { remote, local ->
                //local?.union(remote ?: listOf())
                // TODO we might use intersect and return a set
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

    /**
     * Get the recipe with ingredients and combine them with the saved ones
     */
    override fun getRecipesByIngredients(vararg foodFilter: String?): Flow<Result<List<RecipeIngredients>?>> {
        val remoteRecipes = recipeRemoteDataSource.getRecipesByIngredients(*foodFilter)
        return recipeLocalDataSource.getRecipesIngredients()
            .retryWhen { cause, attempt ->
                retryConnection(cause, attempt)
            }
            .combine(remoteRecipes) { local, remote ->
                //local?.union(remote ?: listOf())
                local?.toMutableList()?.applyRemoteRecipes(remote) // TODO test it
            }
            .map(::recipeIngredientResult)
            .flowOn(Dispatchers.Default)
            .conflate()
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
        recipeRemoteDataSource.getRecipeInfo(id)
    }

    /**
     * Save the recipe or delete it using the flag of RecipeIngredients class
     * // TODO Spoonacular does not have a search recipe with id, call it with id and number = 1
     * // TODO so we can save a reciperesult domain to be used in recipe local view
     */
    override suspend fun saveRecipe(recipe: RecipeIngredients) = withContext(ioDispatcher){
        try {
            if (recipe.recipeId > 0L) {
                val deleteRecipe = recipeLocalDataSource.deleteRecipe(recipe)
                Result.Success(recipe)
            } else {
                // TODO call the search recipe and save the reciperesult domain as well as RecipeIngredients
                    // Use the coroutine sequential by default to retrieve the remote first, then save RecipeResult
                val newRecipe = recipeLocalDataSource.saveRecipe(recipe)
                if (newRecipe != null) {
                    Result.Success(recipeLocalDataSource.getRecipeIngredients(newRecipe.toInt()))
                } else {
                    throw Exception("Recipe not saved")
                }
            }
        } catch (e: Exception) {
            Result.ExError(e)
        }
    }

    override suspend fun initSession(url: String) =
        recipeRemoteDataSource.initSession(url)

    override suspend fun closeSession() =
        recipeRemoteDataSource.closeSession()

    override suspend fun observeStreamRecipes(): Flow<Result<RecipeDomain>> {
        return recipeRemoteDataSource.observeRecipesStream()
            .map {
                // Testing, no need to check values
                Result.Success(it)
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