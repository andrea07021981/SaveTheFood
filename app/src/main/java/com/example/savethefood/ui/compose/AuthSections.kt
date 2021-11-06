package com.example.savethefood.ui.compose

import androidx.annotation.StringRes
import com.example.savethefood.R

enum class AuthSections(
    @StringRes
    val title: Int,
    val route: String
) {
    LOGIN(R.string.log_in, "auth/login"),
    SIGNUP(R.string.sign_up, "auth/signup")
}