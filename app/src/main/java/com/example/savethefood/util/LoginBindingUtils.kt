package com.example.savethefood.util

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.example.savethefood.constants.LoginAuthenticationStates
import com.example.savethefood.constants.LoginAuthenticationStates.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("bind:loadViewAnimation")
fun View.animation(@AnimRes resource: Int) {
    val animation = AnimationUtils.loadAnimation(context, resource)
    startAnimation(animation)
}

@BindingAdapter("bind:hasError")
fun TextInputLayout.hasError(error: Boolean) {
    when (error) {
        true -> setError("Mandatory field")
        else -> setError(null)
    }
}

/**
 * This bindiadapter display the login status using [LoginAuthenticationStates]
 */
@BindingAdapter("bind:loginStatus")
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
        is Idle -> Toast.makeText(context.context, "Idle state", Toast.LENGTH_SHORT).show()
        is Authenticating -> Toast.makeText(context.context, "Authenticating", Toast.LENGTH_SHORT)
            .show()
    }
}