package com.example.savethefood.ui.compose.auth

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.savethefood.R
import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.utils.LoginAuthenticationStates
import com.example.savethefood.shared.utils.LoginStateValue
import com.example.savethefood.shared.viewmodel.LoginState
import com.example.savethefood.shared.viewmodel.LoginViewModel
import com.example.savethefood.ui.compose.SaveTheFoodScaffold
import com.example.savethefood.ui.compose.component.BasicButton
import com.example.savethefood.ui.compose.component.BasicTopAppBar
import com.example.savethefood.ui.compose.component.BasicVerticalSurface
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onUserLogged: (UserDomain?) -> Unit,
    onBack: () -> Unit,
    viewModel: LoginViewModel = getViewModel(),
    authState: AuthState = rememberAuthState()
) {

    val uiState by viewModel.uiState.collectAsState()
    SignUpScreen(
        modifier = modifier,
        authState = authState,
        uiState = uiState,
        signIn = viewModel::onSignInClick,
        onBack = onBack,
        onUserLogged = onUserLogged,
        resetState = viewModel::resetState
    )
}

@Composable
fun SignUpScreen(
    modifier: Modifier,
    authState: AuthState,
    uiState: LoginState,
    signIn: () -> Unit,
    onBack: () -> Unit,
    onUserLogged: (UserDomain?) -> Unit,
    resetState: () -> Unit
) {
    val loginState = uiState.authState
    if (loginState is LoginAuthenticationStates.Authenticated) {
        onUserLogged(loginState.user)
        resetState()
    } else {
        SignUpScreen(
            modifier = modifier,
            authStatus = authState,
            userName = uiState.userName,
            userNameState = uiState.userName.valueStatus.observeAsState().value,
            email = uiState.email,
            emailState = uiState.email.valueStatus.observeAsState().value,
            password = uiState.password,
            passwordState = uiState.password.valueStatus.observeAsState().value,
            onBack = onBack,
            signIn = signIn
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    authStatus: AuthState,
    userName: LoginViewModel.LoginStatus,
    userNameState: LoginStateValue?,
    email: LoginViewModel.LoginStatus,
    emailState: LoginStateValue?,
    password: LoginViewModel.LoginStatus,
    passwordState: LoginStateValue?,
    onBack: () -> Unit,
    signIn: () -> Unit
) {
    SaveTheFoodScaffold(
        backgroundColor = SaveTheFoodTheme.colors.uiBackground,
        contentColor = contentColorFor(backgroundColor = SaveTheFoodTheme.colors.uiBackground),
        topBar = {
            BasicTopAppBar(
                title = {},
                homeButton = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = SaveTheFoodTheme.colors.brand
                        )
                    }
                },
                actions = { /*TODO*/ },
                elevation = 8.dp
            )
        },
        modifier = modifier
    ) { paddingValues ->
        BasicVerticalSurface(
            modifier = modifier.padding(paddingValues = paddingValues)
        ){
            Text(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                color = SaveTheFoodTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
                text = "Let's get started!",
                style = MaterialTheme.typography.h4
            )
            Text(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                color = SaveTheFoodTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
                text = "Create a new account",
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(64.dp).fillMaxWidth())
            AuthForm(
                isLoginIn = false,
                authStatus = authStatus,
                userName = userName,
                userNameState = userNameState,
                email = email,
                emailState = emailState,
                password = password,
                passwordState = passwordState,
                signIn = signIn,
            )
        }
    }
}

@Preview
@Preview("Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSignUpScreen() {
    val authStatus: AuthState = rememberAuthState()
    SaveTheFoodTheme {
        SignUpScreen(
            authStatus = authStatus,
            userName = object : LoginViewModel.LoginStatus("Name") {
                override val checkStatus: (String) -> LoginStateValue = {
                    LoginStateValue.NONE
                }
            },
            userNameState = LoginStateValue.NONE,
            email = object : LoginViewModel.LoginStatus("Email") {
                override val checkStatus: (String) -> LoginStateValue = {
                    LoginStateValue.NONE
                }
            },
            emailState = LoginStateValue.NONE,
            password = object : LoginViewModel.LoginStatus("Password") {
                override val checkStatus: (String) -> LoginStateValue = {
                    LoginStateValue.NONE
                }
            },
            passwordState = LoginStateValue.NONE,
            onBack = {}
        ) {}
    }
}