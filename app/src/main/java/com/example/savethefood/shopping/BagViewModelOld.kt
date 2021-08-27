package com.example.savethefood.shopping

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.shared.data.domain.BagDomain
import com.example.savethefood.shared.data.source.repository.ShoppingRepository
import com.example.savethefood.shared.utils.Event
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform


@Deprecated("Moved to shared")
class BagViewModelOld @ViewModelInject constructor(
    private val shoppingDataRepository: ShoppingRepository,
) : ViewModel(){

    private var _bagDetailEvent = MutableLiveData<Event<BagDomain>>()
    val bagDetailEvent = _bagDetailEvent

    val bagList: LiveData<List<BagDomain>?> =
        shoppingDataRepository.getFoodsInBag()
            .onStart {
                emit(com.example.savethefood.shared.data.Result.Loading)
            }
            .catch { error ->
                emit(com.example.savethefood.shared.data.Result.ExError(Exception(error.message)))
            }
            .transform { value ->
                when (value) {
                    is com.example.savethefood.shared.data.Result.Loading -> Unit
                    is com.example.savethefood.shared.data.Result.Success -> emit(value.data)
                    else -> Unit
                }
            }
            .asLiveData(viewModelScope.coroutineContext)

    fun navigateToBadItemDetail(item: BagDomain = BagDomain()) {
        _bagDetailEvent.value = Event(item)
    }
}