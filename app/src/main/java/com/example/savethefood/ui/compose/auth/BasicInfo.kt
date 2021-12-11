package com.example.savethefood.ui.compose.auth

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.savethefood.R
import com.example.savethefood.shared.utils.LoginStateValue
import com.example.savethefood.shared.viewmodel.LoginViewModel
import com.example.savethefood.ui.compose.component.BasicButton
import com.example.savethefood.ui.compose.component.BasicInputTextfield
import com.example.savethefood.ui.compose.component.BasicVerticalSurface
import com.example.savethefood.ui.compose.extention.hasLoginError
import com.example.savethefood.ui.theme.SaveTheFoodTheme

// TODO pass a remember boolean to hide show specific sections
@Composable
fun AuthForm(
    modifier: Modifier = Modifier,
    isLoginIn: Boolean = true,
    authStatus: AuthState,
    userName: LoginViewModel.LoginStatus,
    userNameState: LoginStateValue?,
    email: LoginViewModel.LoginStatus,
    emailState: LoginStateValue?,
    password: LoginViewModel.LoginStatus,
    passwordState: LoginStateValue?,
    signIn: () -> Unit,
    signUp: () -> Unit = {}
) {
    if (isLoginIn.not()) {
        BasicInputTextfield(
            modifier = Modifier
                .fillMaxWidth(.8F),
            res = R.drawable.person_outline_24,
            label = authStatus.context.resources.getString(R.string.username),
            text = authStatus.userName,
            isError = userNameState?.hasLoginError() == true,
            errorMessage = userNameState?.message ?: "",
            onFocusChanged = {
                authStatus.userNameHasFocus = it.run {
                    if (isFocused.not() && authStatus.userNameHasFocus) {
                        userName.value = authStatus.userName
                        userName.onFocusChanged(userName.value)
                    }
                    isFocused
                }
            },
            imeAction = ImeAction.Next,
            onTextChanged = authStatus.setUserName
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
    BasicInputTextfield(
        modifier = modifier
            .fillMaxWidth(.8F),
        res = R.drawable.email_white_24dp,
        label = authStatus.context.resources.getString(R.string.email),
        text = authStatus.email,
        isError = emailState?.hasLoginError() == true,
        errorMessage = emailState?.message ?: "",
        onFocusChanged = {
            authStatus.emailHasFocus = it.run {
                if (isFocused.not() && authStatus.emailHasFocus) {
                    email.value = authStatus.email
                    email.onFocusChanged(email.value)
                }
                isFocused
            }
        },
        imeAction = ImeAction.Next,
        onTextChanged = authStatus.setEmail
    )
    Spacer(modifier = Modifier.height(16.dp))
    BasicInputTextfield(
        modifier = modifier
            .fillMaxWidth(.8F),
        label = authStatus.context.resources.getString(R.string.password),
        res = R.drawable.lock_white_24dp,
        isPasswordField = true,
        passwordVisibility = authStatus.passwordVisibility,
        onPasswordVisibilityChanged = {
            authStatus.passwordVisibility = !it
        },
        text = authStatus.password,
        isError = passwordState?.hasLoginError() == true,
        errorMessage = passwordState?.message ?: "",
        onFocusChanged = {
            authStatus.passwordHasFocus = it.run {
                if (it.isFocused.not() && authStatus.passwordHasFocus) {
                    password.value = authStatus.password
                    password.onFocusChanged(password.value)
                }
                isFocused
            }
        },
        onTextChanged = authStatus.setPassword,
        onImeAction = { if (isLoginIn) signIn() else ImeAction.Next }
    )
    if (isLoginIn.not()) {
        Spacer(modifier = Modifier.height(16.dp))
        BasicInputTextfield(
            modifier = modifier
                .fillMaxWidth(.8F),
            label = authStatus.context.resources.getString(R.string.confirm_password),
            res = R.drawable.lock_white_24dp,
            isPasswordField = true,
            passwordVisibility = authStatus.passwordVisibility,
            onPasswordVisibilityChanged = {
                authStatus.passwordVisibility = !it
            },
            text = authStatus.password,
            isError = passwordState?.hasLoginError() == true,
            errorMessage = passwordState?.message ?: "",
            onFocusChanged = {
                authStatus.passwordHasFocus = it.run {
                    if (it.isFocused.not() && authStatus.passwordHasFocus) {
                        password.value = authStatus.password
                        password.onFocusChanged(password.value)
                    }
                    isFocused
                }
            },
            onTextChanged = authStatus.setPassword,
            onImeAction = signIn
        )
        Spacer(modifier = Modifier.height(64.dp))
        BasicButton(
            modifier = Modifier
                .fillMaxWidth(.8F)
                .height(60.dp),
            text = R.string.register,
            onClick = signIn
        )
    } else {
        Spacer(modifier = Modifier.height(72.dp))
        BasicButton(
            modifier = Modifier
                .fillMaxWidth(.8F)
                .height(60.dp),
            text = R.string.log_in,
            onClick = signIn
        )
        Spacer(modifier = Modifier.height(32.dp))
        BasicButton(
            modifier = Modifier
                .fillMaxWidth(.8F)
                .height(60.dp),
            text = R.string.sign_up,
            onClick = signUp
        )
    }
}

@Preview
@Preview("Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewBasicInfo() {
    val authStatus: AuthState = rememberAuthState()
    SaveTheFoodTheme {
        BasicVerticalSurface(
            modifier = Modifier
        ) {
            AuthForm(
                authStatus = authStatus,
                userName = object : LoginViewModel.LoginStatus("Name") {
                    override val checkStatus: (String) -> LoginStateValue = {
                        LoginStateValue.NONE
                    }
                },
                userNameState = LoginStateValue.NONE,
                email = object : LoginViewModel.LoginStatus("asd") {
                    override val checkStatus: (String) -> LoginStateValue = {
                        LoginStateValue.NONE
                    }
                },
                emailState = LoginStateValue.NONE,
                password = object : LoginViewModel.LoginStatus("asd") {
                    override val checkStatus: (String) -> LoginStateValue = {
                        LoginStateValue.NONE
                    }
                },
                passwordState = LoginStateValue.NONE,
                signIn = {}
            )
        }
    }
}