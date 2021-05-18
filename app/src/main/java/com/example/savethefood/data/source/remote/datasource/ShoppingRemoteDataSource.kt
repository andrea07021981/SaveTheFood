package com.example.savethefood.data.source.remote.datasource

import com.example.savethefood.data.domain.BagDomain
import com.example.savethefood.data.source.ShoppingDataSource
import com.example.savethefood.data.source.local.dao.ShoppingDatabaseDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingRemoteDataSource @Inject constructor(
) : ShoppingDataSource {

    override fun getFoodsInBag(): Flow<List<BagDomain>?> {
        TODO("Not yet implemented")
    }

    override suspend fun saveItemInBag(item: BagDomain): Long {
        TODO("Not yet implemented")
    }
}