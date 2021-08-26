package com.example.savethefood.shared.utils

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savethefood.shared.constant.Constants
import com.example.savethefood.shared.data.domain.BagDomain
import com.example.savethefood.shared.data.domain.FoodDomain
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// TODO replace result with ApiCallStatus?
/**
 * Dispatchers.Main.immediate is set as the default CoroutineDispatcher for viewModelScope
 */
inline fun <T> ViewModel.launchDataLoad(loader: MutableLiveData<com.example.savethefood.shared.data.Result<T>>, crossinline block: suspend () -> com.example.savethefood.shared.data.Result<T>): Job {
    return viewModelScope.launch {
        try {
            loader.value = com.example.savethefood.shared.data.Result.Loading
            loader.value = block()
        } catch (error: Exception) {
            loader.value = com.example.savethefood.shared.data.Result.ExError(error)
        } finally {
            loader.value = com.example.savethefood.shared.data.Result.Idle
        }
    }
}

fun SavedStateHandle?.retrieveFood(): FoodDomain {
    return this?.get<FoodDomain>(Constants.BUNDLE_FOOD_VALUE) ?: FoodDomain()
}

fun Bundle?.retrieveBag(): BagDomain {
    return this?.get(Constants.BUNDLE_BAG_VALUE)?.let {
        it as BagDomain
    }?: BagDomain()
}