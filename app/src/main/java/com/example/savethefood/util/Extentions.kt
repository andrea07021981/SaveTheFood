package com.example.savethefood.util

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.view.MenuItem
import androidx.appcompat.widget.SearchView

/**
 * Extension function to create a SearchView Item and manage the text inserted
 * @param   activity    Current context
 * @param   hint        Hint for the edit text
 * @param   block       High Order function to manage the text change
 */
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