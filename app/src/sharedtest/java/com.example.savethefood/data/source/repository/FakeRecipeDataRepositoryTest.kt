package com.example.savethefood.data.source.repository

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.domain.RecipeIngredients
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.RecipeDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform

class FakeRecipeDataRepositoryTest(
    private val fakeRecipeLocalDataSourceTest: RecipeDataSource
) : RecipeRepository {
    override fun getRecipes(): Flow<Result<RecipeDomain>> {
        return fakeRecipeLocalDataSourceTest.getRecipes()
            .transform { value ->
                if (value != null) {
                    emit(Result.Success(value))
                } else {
                    emit(Result.Error("No data"))
                }
            }
            .flowOn(Dispatchers.IO)
    }

    override fun getRecipesByIngredients(vararg foodFilter: String?): Flow<Result<List<RecipeIngredients>?>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRecipe(recipe: RecipeResult) {
        TODO("Not yet implemented")
    }

}