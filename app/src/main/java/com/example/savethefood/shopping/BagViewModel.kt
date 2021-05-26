package com.example.savethefood.shopping

import android.os.Bundle
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.constants.Constants
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.BagDomain
import com.example.savethefood.data.domain.FoodItem
import com.example.savethefood.data.source.repository.FoodRepository
import com.example.savethefood.data.source.repository.ShoppingRepository
import com.example.savethefood.util.retrieveBag
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import java.lang.Exception
import kotlin.collections.LinkedHashSet


class BagViewModel @ViewModelInject constructor(
    private val shoppingDataRepository: ShoppingRepository,
    private val foodDataRepository: FoodRepository,
    @Assisted val bag: SavedStateHandle
) : ViewModel(){

    private val _foodBag: BagDomain = bag.get<Bundle>(Constants.BUNDLE_BAG_KEY).retrieveBag()

    private var _bagDetailEvent = MutableLiveData<Event<Unit>>()
    val bagDetailEvent = _bagDetailEvent

    private val _openFoodTypeDialog = MutableLiveData<Event<Unit>>()
    val openFoodTypeDialog: LiveData<Event<Unit>>
        get() = _openFoodTypeDialog

    val errorName = MutableLiveData<Boolean>()
    val errorQuantity = MutableLiveData<Boolean>()

    val bagList: LiveData<Result<List<BagDomain>?>> =
        shoppingDataRepository.getFoodsInBag()
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


    // TODO This code is duplicated with addfood, solve it
    private val foodTypeFilter = MutableLiveData<String>()
    private var _foodsItems: LinkedHashSet<FoodItem>? = null
    val foodItems = foodTypeFilter.switchMap { filter ->
        // We need the livedata constructor since _foodItems is not a live data
        // The constructor livedata is also used in case of suspend fun (not flow)
        liveData {
            if (filter.isNullOrEmpty()) {
                emit(_foodsItems)
            } else {
                emit(_foodsItems?.filter {
                    it.name.contains(filter.toUpperCase())
                })
            }
        }
    }

    val bagDomain: LiveData<BagDomain> = liveData {
        emit(_foodBag) // TODO Usee filters and transformations like add food
    }

    init {
        _foodsItems = foodDataRepository.getFoodImages()
    }

    fun save() {
        // TODO save local bad item
    }

    fun openFoodDialog() {
        _openFoodTypeDialog.value = Event(Unit)
    }

    fun navigateToBadItemDetail() {
        _bagDetailEvent.value = Event(Unit)
    }
}