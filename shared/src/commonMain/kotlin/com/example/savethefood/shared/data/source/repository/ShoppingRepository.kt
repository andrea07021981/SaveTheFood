package com.example.savethefood.shared.data.source.repository

import com.example.savethefood.shared.data.Result
import com.example.savethefood.shared.data.domain.BagDomain
import kotlinx.coroutines.flow.Flow


interface ShoppingRepository {

    @Throws(Exception::class)
    fun getFoodsInBag(): Flow<Result<List<BagDomain>?>>

    @Throws(Exception::class)
    suspend fun saveItemInBag(item: BagDomain): Result<BagDomain>
}