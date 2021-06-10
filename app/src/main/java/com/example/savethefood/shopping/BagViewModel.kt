package com.example.savethefood.shopping

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.BagDomain
import com.example.savethefood.data.source.repository.ShoppingRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform


class BagViewModel @ViewModelInject constructor(
    private val shoppingDataRepository: ShoppingRepository,
) : ViewModel(){

    private var _bagDetailEvent = MutableLiveData<Event<BagDomain>>()
    val bagDetailEvent = _bagDetailEvent

    val bagList: LiveData<List<BagDomain>?> =
        shoppingDataRepository.getFoodsInBag()
            .onStart {
                emit(Result.Loading)
            }
            .catch { error ->
                emit(Result.ExError(Exception(error.message)))
            }
            .transform { value ->
                when (value) {
                    is Result.Loading -> Unit
                    is Result.Success -> emit(value.data)
                    else -> Unit
                }
            }
            .asLiveData(viewModelScope.coroutineContext)

    fun navigateToBadItemDetail(item: BagDomain = BagDomain()) {
        _bagDetailEvent.value = Event(item)
    }
}