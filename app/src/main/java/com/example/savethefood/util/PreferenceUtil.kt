package com.example.savethefood.util

import android.content.Context
import android.content.SharedPreferences
import com.example.savethefood.constants.PreferenceKey

/**
 * Object declaration to manage the preferences 
 */
object PreferenceUtil {

    fun sharedPreference(context: Context?): SharedPreferences? {
        return context?.let {
            it.getSharedPreferences(
                it.packageName + PreferenceKey.PREFERENCES_FILE, Context.MODE_PRIVATE)
        }
    }

    private inline fun SharedPreferences.editPref(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    fun SharedPreferences.Editor.put(pair: Pair<String, Any>) {
        val key = pair.first
        when (val value = pair.second) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> error("Only primitive types can be stored in SharedPreferences")
        }
    }

    /*var SharedPreferences.filterFoodPreference
        get() = getString(FILTER_FOOD_KEY, null)
        set(value) {
            editPref {
                it.putString(FILTER_FOOD_KEY, value)
            }
        }*/
}