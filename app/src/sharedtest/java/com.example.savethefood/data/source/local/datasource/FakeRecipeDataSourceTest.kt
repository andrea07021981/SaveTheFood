package com.example.savethefood.data.source.local.datasource

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.domain.RecipeIngredients
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.RecipeDataSource
import com.example.savethefood.data.source.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRecipeDataSourceTest(
    private val recipeResult: List<RecipeResult>
) : RecipeDataSource {
    override fun getRecipes(): Flow<RecipeDomain?> = flow {
        recipeResult
    }

    override fun getRecipesByIngredients(vararg foodFilter: String?): Flow<List<RecipeIngredients>?> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRecipe(recipe: RecipeIngredients): RecipeIngredients? {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeIngredients(recipeId: Int): RecipeIngredients? {
        TODO("Not yet implemented")
    }

    override fun getRecipesIngredients(): Flow<List<RecipeEntity>?> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecipe(recipeId: RecipeIngredients): Int? {
        TODO("Not yet implemented")
    }
}