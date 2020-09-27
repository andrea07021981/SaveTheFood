package com.example.savethefood.data.source.local.datasource

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.RecipeDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRecipeDataSourceTest(
    private val recipeResult: List<RecipeResult>
) : RecipeDataSource {
    override fun getRecipes(foodFilter: String?): Flow<Result<RecipeDomain>> {
        return flow {
            recipeResult.filter { it.title == foodFilter }
        }
    }

    override suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRecipe(recipe: RecipeInfoDomain) {
        TODO("Not yet implemented")
    }

}