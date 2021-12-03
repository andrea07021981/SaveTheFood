package com.example.savethefood.ui.compose.auth

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.savethefood.R
import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.utils.LoginAuthenticationStates
import com.example.savethefood.shared.utils.LoginStateValue
import com.example.savethefood.shared.viewmodel.LoginViewModel
import com.example.savethefood.ui.compose.component.BasicButton
import com.example.savethefood.ui.compose.component.BasicInputTextfield
import com.example.savethefood.ui.compose.component.BasicVerticalSurface
import com.example.savethefood.ui.compose.extention.hasLoginError
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import org.jetbrains.annotations.NotNull
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onUserLogged: (UserDomain?) -> Unit,
    viewModel: LoginViewModel = getViewModel()
) {

    val authStatus: AuthState = rememberAuthState(
        userName = "Name",
        userNameFocus = false,
        email = "a@a.com",
        emailFocus = false,
        password = "aaaaaaaa",
        passwordFocus = false
    )

    val uiState by viewModel.uiState.collectAsState()

    val loginState = uiState.authState
    if (loginState is LoginAuthenticationStates.Authenticated) {
        onUserLogged(loginState.user)
        viewModel.resetState()
    }
    val navToSignUp by viewModel.navigateToSignUp.observeAsState()
    LaunchedEffect(navToSignUp) {
        if (navToSignUp?.hasBeenHandled == false) {
            when (navToSignUp?.peekContent()) {
                is Unit -> onUserLogged(null)
            }
        }
    }

    LoginScreen(
        modifier = modifier,
        authStatus = authStatus,
        email = uiState.email,
        emailState = uiState.email.valueStatus.observeAsState().value,
        password = uiState.password,
        passwordState = uiState.password.valueStatus.observeAsState().value,
        signIn = viewModel::onSignInClick,
        signUp = viewModel::moveToSignUp
    )
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    authStatus: AuthState,
    email: LoginViewModel.LoginStatus,
    emailState: LoginStateValue?,
    password: LoginViewModel.LoginStatus,
    passwordState: LoginStateValue?,
    signIn: () -> Unit,
    signUp: () -> Unit
) {
    BasicVerticalSurface(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier.size(100.dp),
            alignment = Alignment.TopCenter,
            painter = painterResource(id = R.drawable.ic_food),
            contentDescription = "Logo"
        )
        Spacer(modifier = Modifier.height(170.dp))
        BasicInputTextfield(
            modifier = Modifier
                .fillMaxWidth(.8F)
                .onFocusChanged {
                    authStatus.emailHasFocus = it.run {
                        if (isFocused.not() && authStatus.emailHasFocus) {
                            email.value = authStatus.email
                            email.onFocusChanged(email.value)
                        }
                        isFocused
                    }
                },
            res = R.drawable.email_white_24dp,
            label = authStatus.context.resources.getString(R.string.username),
            text = authStatus.email,
            isError = emailState?.hasLoginError() == true,
            errorMessage = emailState?.message ?: "",
            imeAction = ImeAction.Next,
            onTextChanged = authStatus.setEmail
        )
        Spacer(modifier = Modifier.height(16.dp))
        BasicInputTextfield(
            modifier = Modifier
                .fillMaxWidth(.8F)
                .onFocusChanged {
                    authStatus.passwordHasFocus = it.run {
                        if (it.isFocused.not() && authStatus.passwordHasFocus) {
                            password.value = authStatus.password
                            password.onFocusChanged(password.value)
                        }
                        isFocused
                    }
                },
            label = authStatus.context.resources.getString(R.string.password),
            res = R.drawable.email_white_24dp,
            isPasswordField = true,
            passwordVisibility = authStatus.passwordVisibility,
            onPasswordVisibilityChanged = {
                authStatus.passwordVisibility = !it
            },
            text = authStatus.password,
            isError = passwordState?.hasLoginError() == true,
            errorMessage = passwordState?.message ?: "",
            onTextChanged = authStatus.setPassword,
            onImeAction = signIn
        )
        Spacer(modifier = Modifier.height(64.dp))
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
fun PreviewLoginScreen() {
    val authStatus: AuthState = rememberAuthState(
        userName = "Name",
        userNameFocus = false,
        email = "a@a.com",
        emailFocus = false,
        password = "aaaaaaaa",
        passwordFocus = false
    )
    SaveTheFoodTheme {
        LoginScreen(
            authStatus = authStatus,
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
            signIn = {},
            signUp = {}
        )
    }
}