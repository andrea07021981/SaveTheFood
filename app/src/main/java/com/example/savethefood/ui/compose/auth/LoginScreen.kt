package com.example.savethefood.ui.compose.auth

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.example.savethefood.ui.compose.component.BasicSnackBar
import com.example.savethefood.ui.compose.component.BasicTopAppBar
import com.example.savethefood.ui.compose.component.BasicVerticalSurface
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import com.google.accompanist.insets.systemBarsPadding
import org.koin.androidx.compose.getViewModel

// TODO Handle the login error here or state hoisting and let the base Scaffold to use the Snackbar host
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onUserLogged: (UserDomain?) -> Unit,
    onSignUp: () -> Unit,
    viewModel: LoginViewModel = getViewModel(),
    authState: AuthState = rememberAuthState()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scaffoldState = rememberScaffoldState()

    SaveTheFoodScaffold(
        backgroundColor = SaveTheFoodTheme.colors.uiBackground,
        contentColor = contentColorFor(backgroundColor = SaveTheFoodTheme.colors.uiBackground),
        topBar = {
            BasicTopAppBar(
                title = {},
                homeButton = {},
                actions = {},
                elevation = 8.dp
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = it,
                modifier = Modifier.systemBarsPadding(),
                snackbar = { data -> BasicSnackBar(data) }
            )
        },
        modifier = modifier,
        scaffoldState = scaffoldState
    ) { paddingValues ->
        LoginScreen(
            modifier = modifier.padding(paddingValues = paddingValues),
            authState = authState,
            uiState = uiState,
            signIn = viewModel::onSignInClick,
            signUp = onSignUp,
            onUserLogged = onUserLogged,
            resetState = viewModel::resetState
        )
    }

    // Show the snackbar in case of error
    if (uiState.authState is LoginAuthenticationStates.InvalidAuthentication) {
        val message = (uiState.authState as LoginAuthenticationStates.InvalidAuthentication).message
        val messageText: String = message

        LaunchedEffect(messageText, scaffoldState) {
            scaffoldState.snackbarHostState.showSnackbar(messageText)
            viewModel.resetState()
        }
    }
}

/**
 * TODO add the other states:
 * Loading -> Recomposition occurs when the uiState changes, so create a loadeer beetween image and email
 * Failed -> User a scaffold with simple top app bar and snackbar for errors
 * Success -> call the onUserLogged as above
 *
 */

@Composable
fun LoginScreen(
    modifier: Modifier,
    authState: AuthState,
    uiState: LoginState,
    signIn: () -> Unit,
    signUp: () -> Unit,
    onUserLogged: (UserDomain?) -> Unit,
    resetState: () -> Unit
) {
    val loginState = uiState.authState
    if (loginState is LoginAuthenticationStates.Authenticated) {
        onUserLogged(loginState.user)
        resetState()
    } else {
        // Manage here the other states
        LoginScreen(
            modifier = modifier,
            authStatus = authState,
            userName = uiState.userName,
            userNameState = uiState.userName.valueStatus.observeAsState().value,
            email = uiState.email,
            emailState = uiState.email.valueStatus.observeAsState().value,
            password = uiState.password,
            passwordState = uiState.password.valueStatus.observeAsState().value,
            signIn = signIn,
            signUp = signUp
        )
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    authStatus: AuthState,
    userName: LoginViewModel.LoginStatus,
    userNameState: LoginStateValue?,
    email: LoginViewModel.LoginStatus,
    emailState: LoginStateValue?,
    password: LoginViewModel.LoginStatus,
    passwordState: LoginStateValue?,
    signIn: () -> Unit,
    signUp: () -> Unit
) {
    BasicVerticalSurface(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            modifier = Modifier.size(100.dp),
            alignment = Alignment.TopCenter,
            painter = painterResource(id = R.drawable.ic_food),
            contentDescription = "Logo"
        )
        Spacer(modifier = Modifier.height(160.dp))
        AuthForm(
            authStatus = authStatus,
            userName = userName,
            userNameState = userNameState,
            email = email,
            emailState = emailState,
            password = password,
            passwordState = passwordState,
            signIn = signIn,
            signUp = signUp
        )
    }
}

@Preview
@Preview("Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewLoginScreen() {
    val authStatus: AuthState = rememberAuthState()
    SaveTheFoodTheme {
        LoginScreen(
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
            signIn = {},
            signUp = {}
        )
    }
}