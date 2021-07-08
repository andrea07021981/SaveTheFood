package com.example.savethefood.shared.data.source

import com.example.savethefood.shared.data.domain.BagDomain
import kotlinx.coroutines.flow.Flow

interface ShoppingDataSource {

    @Throws(Exception::class)
    fun getFoodsInBag(): Flow<List<BagDomain>?>

    @Throws(Exception::class)
    suspend fun saveItemInBag(item: BagDomain): Long
}