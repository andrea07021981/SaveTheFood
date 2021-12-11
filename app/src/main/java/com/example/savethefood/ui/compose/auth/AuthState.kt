package com.example.savethefood.ui.compose.auth

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.example.savethefood.BuildConfig

/**
 * Authentication state holder, default values are for testing purpose
 */
@Composable
fun rememberAuthState(
    userName: String = "",
    userNameFocus: Boolean = false,
    email: String = "",
    emailFocus: Boolean = false,
    password: String = "",
    passwordFocus: Boolean = false,
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
    var initUserName: String,
    initUserNameHasFocus: Boolean,
    var initEmail: String,
    initEmailHasFocus: Boolean,
    var initPassword: String,
    initPasswordHasFocus: Boolean,
    var context: Context
) {

    init {
        if (BuildConfig.DEBUG) {
            initUserName = "Name"
            initEmail = "a@a.com"
            initPassword = "aaaaaaaa"
        }
    }

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