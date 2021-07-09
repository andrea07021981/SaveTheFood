package com.example.savethefood.shared.data.source.remote.datasource

import com.example.savethefood.shared.data.domain.BagDomain
import com.example.savethefood.shared.data.source.ShoppingDataSource
import kotlinx.coroutines.flow.Flow


class ShoppingRemoteDataSource(

) : ShoppingDataSource {

    override fun getFoodsInBag(): Flow<List<BagDomain>?> {
        TODO("Not yet implemented")
    }

    override suspend fun saveItemInBag(item: BagDomain): Long {
        TODO("Not yet implemented")
    }
}
