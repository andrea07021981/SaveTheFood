package com.example.savethefood.shopping

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.BagDomain
import com.example.savethefood.data.source.repository.ShoppingRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import java.lang.Exception


class BagViewModel @ViewModelInject constructor(
    shoppingRepository: ShoppingRepository
) : ViewModel(){

    val bagList: LiveData<Result<List<BagDomain>?>> =
        shoppingRepository.getFoodsInBag()
            .onStart {
                emit(Result.Loading)
            }
            .catch { error ->
                emit(Result.ExError(Exception(error.message)))
            }
            .transform { value ->
                emit(value)
            }
            .asLiveData(viewModelScope.coroutineContext)
}