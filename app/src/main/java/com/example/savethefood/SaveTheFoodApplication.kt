package com.example.savethefood

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.example.savethefood.work.RefreshDataWorker
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class SaveTheFoodApplication : Application(), Configuration.Provider{

    private val applicationScope = CoroutineScope(Dispatchers.Default)
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()
        // Obtain the FirebaseAnalytics instance.
        FirebaseApp.initializeApp(this)
        firebaseAnalytics = Firebase.analytics

        //Test
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {
            param(FirebaseAnalytics.Param.ITEM_ID, 1)
            param(FirebaseAnalytics.Param.ITEM_NAME, "test")
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        }
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

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}