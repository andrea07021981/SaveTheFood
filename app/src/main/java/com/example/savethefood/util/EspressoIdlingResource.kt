package com.example.savethefood.util

import androidx.test.espresso.idling.CountingIdlingResource

/**
 * It's used in the app to track whether long running task are stil working
 */
object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"

    //countingIdlingResource > 0 ? app is working, <= o not working
    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
    // Espresso does not work well with coroutines yet. See
    // https://github.com/Kotlin/kotlinx.coroutines/issues/982
    EspressoIdlingResource.increment() // Set app as busy.
    return try {
        function()
    } finally {
        EspressoIdlingResource.decrement() // Set app as idle.
    }
}