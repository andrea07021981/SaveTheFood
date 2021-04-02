package com.example.savethefood.data.source.repository

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.source.RecipeDataSource
import com.example.savethefood.data.source.local.datasource.FakeRecipeDataSourceTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform

class FakeRecipeDataRepositoryTest(
    private val fakeRecipeLocalDataSourceTest: RecipeDataSource
) : RecipeRepository {
    override fun getRecipes(vararg foodFilter: String?): Flow<Result<RecipeDomain>> {
        return fakeRecipeLocalDataSourceTest.getRecipes(*foodFilter)
            .transform { value ->
                if (value != null) {
                    emit(Result.Success(value))
                } else {
                    emit(Result.Error("No data"))
                }
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRecipe(recipe: RecipeInfoDomain) {
        TODO("Not yet implemented")
    }

}