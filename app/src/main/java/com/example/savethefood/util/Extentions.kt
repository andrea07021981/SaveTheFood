package com.example.savethefood.util

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savethefood.constants.Constants
import com.example.savethefood.constants.FoodOrder
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Extension function to create a SearchView Item and manage the text inserted
 * @param   activity    Current context
 * @param   hint        Hint for the edit text
 * @param   block       High Order function to manage the text change
 */
// TODO add onchange like change password
fun MenuItem.configSearchView(activity: Activity, hint: String = "Type Value", block: (String) -> Unit) {
    val searchView = actionView as SearchView
    val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager

    with(searchView) {
        setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))

        //Manage search view
        //making the searchview consume all the toolbar when open
        let { it ->
            it.maxWidth= Int.MAX_VALUE
            it.queryHint = hint
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextChange(newText: String): Boolean {
                    //action while typing
                    block(newText)
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    //action when type Enter
                    return false
                }

            })
        }
    }
}

// TODO replace result with ApiCallStatus?
/**
 * Dispatchers.Main.immediate is set as the default CoroutineDispatcher for viewModelScope
 */
inline fun <T> ViewModel.launchDataLoad(loader: MutableLiveData<Result<T>>, crossinline block: suspend () -> Result<T>): Job {
    return viewModelScope.launch {
        try {
            loader.value = Result.Loading
            loader.value = block()
        } catch (error: Exception) {
            loader.value = Result.ExError(error)
        } finally {
            loader.value = Result.Idle
        }
    }
}

fun Double?.isValidDouble(): Boolean {
    return this != null && this != 0.0
}

fun String.isValidEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): Boolean = this.length in 8..16

// TODO find a way to create a generic ext and avoid the repetition of sortedBy
fun List<FoodDomain>.customSortBy(order: FoodOrder): List<FoodDomain> {
    return when (order) {
        FoodOrder.TITLE -> sortedBy(FoodDomain::title)
        FoodOrder.BEFORE -> sortedBy(FoodDomain::bestBefore)
        FoodOrder.QUANTITY -> sortedBy(FoodDomain::quantity)
        else -> this
    }
}

fun <T> List<T>.isListOfNulls(): Boolean = this.all { it == null }

// TODO hide and show the shimmer
fun ShimmerFrameLayout.start() {
    startShimmer()
}

fun ShimmerFrameLayout.stop() {
    stopShimmer()
}

fun Bundle?.retrieveFood(): FoodDomain {
    return this?.get(Constants.BUNDLE_FOOD_VALUE)?.let {
        it as FoodDomain
    }?: FoodDomain()
}