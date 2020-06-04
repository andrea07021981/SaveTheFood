package com.example.savethefood

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics




class SaveTheFoodApplication : Application(){

    //TODO Add worker to update food, recipes and analytics

    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    override fun onCreate() {
        super.onCreate()
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
}