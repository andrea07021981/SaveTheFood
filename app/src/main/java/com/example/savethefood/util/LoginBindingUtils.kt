package com.example.savethefood.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.example.savethefood.constants.LoginAuthenticationStates
import com.example.savethefood.constants.LoginAuthenticationStates.*
import com.example.savethefood.constants.LoginError
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_food.view.*


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

@BindingAdapter("bind:hasLoginError")
fun TextInputLayout.hasLoginError(error: LoginError) {
    when (error) {
        LoginError.INVALID_USERNAME, LoginError.INVALID_EMAIL, LoginError.INVALID_PASSWORD -> setError(error.message)
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

@BindingAdapter("bind:onFocusChange")
fun binTextFocusChangeCheck(view: TextInputEditText, block: (String) -> Unit) {
    view.setOnFocusChangeListener { v, hasFocus ->
        if (!hasFocus) block((v as TextInputEditText).text.toString())
    }
}

@BindingAdapter("bind:onTextChange")
fun binTextChangeCheck(view: TextInputEditText, block: (String) -> Unit) {
    view.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            block(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {

        }

    })
}