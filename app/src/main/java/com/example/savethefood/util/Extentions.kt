package com.example.savethefood.util

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.*
import com.example.savethefood.constants.Constants
import com.example.savethefood.constants.FoodOrder
import com.example.savethefood.shared.data.domain.BagDomain
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.utils.LoginStateValue
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
        let {
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

fun Bundle?.retrieveBag(): BagDomain {
    return this?.get(Constants.BUNDLE_BAG_VALUE)?.let {
        it as BagDomain
    }?: BagDomain()
}

fun <T> List<T>?.getResult(): com.example.savethefood.shared.data.Result<List<T>> {
    return this?.let {
        if (it.count() > 0) {
            com.example.savethefood.shared.data.Result.Success(it)
        } else {
            com.example.savethefood.shared.data.Result.Error("No data")
        }
    } ?: com.example.savethefood.shared.data.Result.ExError(java.lang.Exception("Error retrieving data"))
}

fun String.getResourceByName(context: Context): Int =
    context.resources.getIdentifier(this, "drawable", context.packageName)


/**
 * Extension function to observe StateFlow like Livedata easily in Fragment/Activity with no
 * boilerplate code
 * FIXME read here https://medium.com/androiddevelopers/repeatonlifecycle-api-design-story-8670d1a7d333
 */
/*
@OptIn(InternalCoroutinesApi::class)
inline fun <T> Flow<T>.collectWhen(
    lifecycleOwner: LifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend (value: T) -> Unit
) {
    lifecycleOwner.addRepeatingJob(state) {
        collect {
            action(it)
        }
    }
}*/
