package com.example.savethefood.ui.compose.auth

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.savethefood.R
import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.utils.LoginAuthenticationStates
import com.example.savethefood.shared.utils.LoginStateValue
import com.example.savethefood.shared.viewmodel.LoginViewModel
import com.example.savethefood.ui.compose.SaveTheFoodScaffold
import com.example.savethefood.ui.compose.component.BasicInputTextfield
import com.example.savethefood.ui.compose.component.BasicTopAppBar
import com.example.savethefood.ui.compose.component.BasicVerticalSurface
import com.example.savethefood.ui.compose.extention.hasLoginError
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onUserLogged: (UserDomain?) -> Unit,
    onBack: () -> Unit,
    viewModel: LoginViewModel = getViewModel()
) {

    // FIXME create all in one view called AuthScreen, too much redundant code
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

    SignUpScreen(
        modifier = modifier,
        authStatus = authStatus,
        userName = uiState.userName,
        userNameState = uiState.userName.valueStatus.observeAsState().value,
        email = uiState.email,
        emailState = uiState.email.valueStatus.observeAsState().value,
        password = uiState.password,
        passwordState = uiState.password.valueStatus.observeAsState().value,
        onBack = onBack,
        signIn = viewModel::onSignInClick
    )
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
            modifier = modifier.padding(paddingValues = paddingValues),
            verticalArrangement = Arrangement.Top
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
            BasicInputTextfield(
                modifier = Modifier
                    .fillMaxWidth(.8F)
                    .onFocusChanged {
                        authStatus.userNameHasFocus = it.run {
                            if (isFocused.not() && authStatus.userNameHasFocus) {
                                userName.value = authStatus.userName
                                userName.onFocusChanged(userName.value)
                            }
                            isFocused
                        }
                    },
                res = R.drawable.person_outline_24,
                label = authStatus.context.resources.getString(R.string.username),
                text = authStatus.userName,
                isError = userNameState?.hasLoginError() == true,
                errorMessage = userNameState?.message ?: "",
                imeAction = ImeAction.Next,
                onTextChanged = authStatus.setUserName
            )
            Spacer(modifier = Modifier.height(16.dp))
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
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onTextChanged = authStatus.setEmail
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Preview("Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSignUpScreen() {
    val authStatus: AuthState = rememberAuthState(
        userName = "Name",
        userNameFocus = false,
        email = "a@a.com",
        emailFocus = false,
        password = "aaaaaaaa",
        passwordFocus = false
    )
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