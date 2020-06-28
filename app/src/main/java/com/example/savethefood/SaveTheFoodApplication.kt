package com.example.savethefood

import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.work.*
import com.example.savethefood.work.RefreshDataWorker
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class SaveTheFoodApplication : Application(){

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    private var firebaseAnalytics: FirebaseAnalytics? = null
    override fun onCreate() {
        super.onCreate()
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Test
        val bundle = Bundle()
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 588)
        firebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, bundle)
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setUpRecurringWork()
        }
    }

    private fun setUpRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRequest
                = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)
    }
}