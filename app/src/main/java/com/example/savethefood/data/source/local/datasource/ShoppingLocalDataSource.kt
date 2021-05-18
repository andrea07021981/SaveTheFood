package com.example.savethefood.data.source.local.datasource

import com.example.savethefood.data.domain.BagDomain
import com.example.savethefood.data.domain.asDatabaseModel
import com.example.savethefood.data.source.ShoppingDataSource
import com.example.savethefood.data.source.local.dao.ShoppingDatabaseDao
import com.example.savethefood.data.source.local.entity.asDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingLocalDataSource @Inject constructor(
    private val shoppingDatabaseDao: ShoppingDatabaseDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ShoppingDataSource{

    override fun getFoodsInBag(): Flow<List<BagDomain>?> {
        return shoppingDatabaseDao.observeFoodsInBag().asDomainModel()
    }

    override suspend fun saveItemInBag(item: BagDomain): Long {
        return shoppingDatabaseDao.insertFoodInBag(item.asDatabaseModel())
    }

}