package com.example.savethefood.data.source.repository

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.BagDomain
import com.example.savethefood.data.domain.RecipeIngredients
import com.example.savethefood.data.source.ShoppingDataSource
import com.example.savethefood.util.getResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

class ShoppingDataRepository @Inject constructor(
    @field:[Named("ShoppingLocalDataSource")] private val shoppingLocalDataSource: ShoppingDataSource,
    @field:[Named("ShoppingRemoteDataSource")] private val shoppingRemoteDataSource: ShoppingDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ShoppingRepository {

    override fun getFoodsInBag(): Flow<Result<List<BagDomain>?>> {
        return shoppingLocalDataSource.getFoodsInBag()
            .retryWhen { cause, attempt ->
                // TODO same as recipe, find another way
                if (cause is IOException && attempt < 5) {    // retry on IOException
                    delay(10000)                     // delay for one second before retry
                    true
                } else {                                      // do not retry otherwise
                    false
                }
            }
            .map { list ->
                list.getResult()
            }
            .flowOn(Dispatchers.Default)
            .conflate()
    }

    override suspend fun saveItemInBag(item: BagDomain): Long {
        TODO("Not yet implemented")
    }
}