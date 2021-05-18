package com.example.savethefood.data.source.repository

import com.example.savethefood.data.domain.BagDomain
import com.example.savethefood.data.source.RecipeDataSource
import com.example.savethefood.data.source.ShoppingDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class ShoppingDataRepository @Inject constructor(
    @field:[Named("ShoppingLocalDataSource")] private val shoppingLocalDataSource: ShoppingDataSource,
    @field:[Named("ShoppingRemoteDataSource")] private val shoppingRemoteDataSource: ShoppingDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ShoppingRepository {

    override fun getFoodsInBag(): Flow<List<BagDomain>?> {
        TODO("Not yet implemented")
    }

    override suspend fun saveItemInBag(item: BagDomain): Long {
        TODO("Not yet implemented")
    }
}