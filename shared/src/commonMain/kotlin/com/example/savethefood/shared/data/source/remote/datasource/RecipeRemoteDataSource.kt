package com.example.savethefood.shared.data.source.remote.datasource
/*


class RecipeRemoteDataSource @Inject constructor(
    private val foodApi: FoodService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecipeDataSource{

    @Throws(Exception::class)
    override fun getRecipes(): Flow<RecipeDomain?> = flow {
        try {
            val recipes = foodApi.getRecipes()
            emit(recipes.asDomainModel())
        } catch (error: Exception) {
            Log.d("Error", error.localizedMessage)
            throw error
        }
    }

    override suspend fun getRecipeById(id: Int): RecipeDomain? {
        try {
            val recipe = foodApi.getRecipesById(id = id)
            delay(10000)
            return recipe?.let {
                it.asDomainModel()
            }
        } catch (error: Exception) {
            Log.d("Error", error.localizedMessage)
            throw error
        }
    }

    @Throws(Exception::class)
    override fun getRecipesByIngredients(vararg foodFilter: String?): Flow<List<RecipeIngredients>?>  = flow {
        try {
            //NEVER USE THE WITHCONTEXT TO CHANGE THE CONTEXT, USE FLOW ON WHICH EXECUTE IN A SECOND THREAD AND CONTEXT
            val recipes = if (foodFilter.toList().isListOfNulls()) {
                foodApi.getRecipesByIngredient(null)
            } else {
                foodApi.getRecipesByIngredient(foodFilter.joinToString(","))
            }
            emit(recipes.asDomainModel())
        } catch (error: Exception) {
            Log.d("Error", error.localizedMessage)
            throw error
        }
    }

    @Throws(Exception::class)
    override suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain> = coroutineScope {
        return@coroutineScope try {
            val recipe = foodApi.getRecipeInfo(id).await()
            // TODO replace result with Apistatus, use result in repository with a converter
            Result.Success(recipe.asDomainModel())
        } catch (error: Exception) {
            Result.ExError(error)
        }
    }

    override suspend fun saveRecipe(recipe: RecipeIngredients): RecipeIngredients? {
        return null
    }

    override suspend fun getRecipeIngredients(recipeId: Int): RecipeIngredients? {
        return null
    }

    override fun getRecipesIngredients(): Flow<List<RecipeIngredients>?> {
        return flow {  }
    }

    override suspend fun deleteRecipe(recipeId: RecipeIngredients): Int? {
        return null
    }
}*/
