package com.example.savethefood.login

import android.content.res.Resources
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.annotation.AnimRes
import androidx.databinding.BindingAdapter


@BindingAdapter("loadAnimation")
fun loadAnimation(image: ImageView, @AnimRes resource: Int) {
    val animation = AnimationUtils.loadAnimation(image.context, resource)
    image.startAnimation(animation)
}