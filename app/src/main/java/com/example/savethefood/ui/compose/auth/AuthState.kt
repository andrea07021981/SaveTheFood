package com.example.savethefood.ui.compose.auth

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberAuthState(
    userName: String,
    userNameFocus: Boolean,
    email: String,
    emailFocus: Boolean,
    password: String,
    passwordFocus: Boolean,
    context: Context = LocalContext.current,
) = remember {
    AuthState(
        initUserName = userName,
        initUserNameHasFocus = userNameFocus,
        initEmail = email,
        initEmailHasFocus = emailFocus,
        initPassword = password,
        initPasswordHasFocus = passwordFocus,
        context = context
    )
}

class AuthState(
    initUserName: String,
    initUserNameHasFocus: Boolean,
    initEmail: String,
    initEmailHasFocus: Boolean,
    initPassword: String,
    initPasswordHasFocus: Boolean,
    var context: Context
) {

    var userName by mutableStateOf(initUserName)
    val setUserName: (String) -> Unit = {
        userName = it
    }
    var userNameHasFocus by mutableStateOf(initUserNameHasFocus)

    var email by mutableStateOf(initEmail)
    val setEmail: (String) -> Unit = {
        email = it
    }
    var emailHasFocus by mutableStateOf(initEmailHasFocus)

    var password by mutableStateOf(initPassword)
    val setPassword: (String) -> Unit = {
        password = it
    }
    var passwordHasFocus by mutableStateOf(initPasswordHasFocus)

    var passwordVisibility by mutableStateOf(false)
}