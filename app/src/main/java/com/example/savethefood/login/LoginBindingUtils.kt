package com.example.savethefood.login

import android.content.res.Resources
import android.text.Layout
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.AnimRes
import androidx.databinding.BindingAdapter



@BindingAdapter("loadViewAnimation")
fun View.animation(@AnimRes resource: Int) {
    val animation = AnimationUtils.loadAnimation(context, resource)
    startAnimation(animation)
}