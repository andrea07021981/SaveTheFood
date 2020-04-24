package com.example.savethefood.util

import android.content.res.Resources
import android.text.Layout
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.databinding.BindingAdapter
import com.example.savethefood.constants.Authenticated
import com.example.savethefood.constants.InvalidAuthentication
import com.example.savethefood.constants.LoginAuthenticationStates
import com.example.savethefood.constants.Unauthenticated
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("loadViewAnimation")
fun View.animation(@AnimRes resource: Int) {
    val animation = AnimationUtils.loadAnimation(context, resource)
    startAnimation(animation)
}

@BindingAdapter("hasError")
fun TextInputLayout.hasError(error: Boolean) {
    when (error) {
        true -> setError("Mandatory field")
        else -> setError(null)
    }
}

/**
 * This bindiadapter display the login status using [LoginAuthenticationStates]
 */
@BindingAdapter("loginStatus")
fun bindStatus(context: View, status: LoginAuthenticationStates?) {
    when (status) {
        is Authenticated -> {
            Toast.makeText(context.context, "Logged", Toast.LENGTH_SHORT).show()
        }
        is Unauthenticated -> {
            Toast.makeText(context.context, "Not logged", Toast.LENGTH_SHORT).show()
        }
        is InvalidAuthentication -> {
            Toast.makeText(context.context, "Error Login", Toast.LENGTH_SHORT).show()
        }
    }
}

