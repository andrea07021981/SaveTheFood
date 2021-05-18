package com.example.savethefood.data.source

import com.example.savethefood.data.domain.BagDomain
import com.example.savethefood.data.domain.RecipeDomain
import kotlinx.coroutines.flow.Flow

interface ShoppingDataSource {

    @Throws(Exception::class)
    fun getFoodsInBag(): Flow<List<BagDomain>?>

    @Throws(Exception::class)
    suspend fun saveItemInBag(item: BagDomain): Long
}