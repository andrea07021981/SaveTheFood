package com.example.savethefood.shared.data.source.repository

import com.example.savethefood.shared.data.Result
import com.example.savethefood.shared.data.domain.BagDomain
import com.example.savethefood.shared.data.source.ShoppingDataSource
import com.example.savethefood.shared.utils.getResult
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class ShoppingDataRepository(
    private val shoppingLocalDataSource: ShoppingDataSource,
    private val shoppingRemoteDataSource: ShoppingDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
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

    override suspend fun saveItemInBag(item: BagDomain): Result<BagDomain> = withContext(Dispatchers.Default) {
        val insertFoodId = shoppingLocalDataSource.saveItemInBag(item)
        if (insertFoodId > 0) {
            return@withContext Result.Success(item)
        } else {
            return@withContext Result.Error("Error saving food")
        }
    }
}