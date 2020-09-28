package com.example.savethefood.data.source.repository

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.source.RecipeDataSource
import com.example.savethefood.data.source.local.datasource.FakeRecipeDataSourceTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRecipeDataRepositoryTest(
    private val fakeRecipeLocalDataSourceTest: RecipeDataSource
) : RecipeRepository {
    override fun getRecipes(foodFilter: String?): Flow<Result<RecipeDomain>> {
        return fakeRecipeLocalDataSourceTest.getRecipes(foodFilter)
    }

    override suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRecipe(recipe: RecipeInfoDomain) {
        TODO("Not yet implemented")
    }

}