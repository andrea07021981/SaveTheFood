package com.example.savethefood.util

import android.widget.EditText
import androidx.databinding.BindingAdapter

/**
 * Needs to be used with [NumberOfSetsConverters.setArrayToString].
 */
@BindingAdapter("numberOfSets")
fun setNumberOfSets(view: EditText, value: String) {
    view.setText(value)
}