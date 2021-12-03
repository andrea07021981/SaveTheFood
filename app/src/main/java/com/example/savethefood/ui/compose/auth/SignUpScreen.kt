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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.utils.LoginAuthenticationStates
import com.example.savethefood.shared.utils.LoginStateValue
import com.example.savethefood.shared.viewmodel.LoginViewModel
import com.example.savethefood.ui.compose.SaveTheFoodScaffold
import com.example.savethefood.ui.compose.component.BasicTopAppBar
import com.example.savethefood.ui.compose.component.BasicVerticalSurface
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onUserLogged: (UserDomain?) -> Unit,
    onBack: () -> Unit,
    viewModel: LoginViewModel = getViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    val loginState = uiState.authState
    if (loginState is LoginAuthenticationStates.Authenticated) {
        onUserLogged(loginState.user)
        viewModel.resetState()
    }

    SignUpScreen(
        modifier = modifier,
        email = uiState.email,
        onBack = onBack,
        signIn = viewModel::onSignInClick
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    email: LoginViewModel.LoginStatus,
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
    ) {
        CompositionLocalProvider(
            LocalContentColor provides SaveTheFoodTheme.colors.textSecondary
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Let's get started!",
                style = MaterialTheme.typography.h3
            )
            Spacer(modifier = Modifier.height(120.dp))
            BasicVerticalSurface(
                modifier = modifier,
            ){
                Text(
                    textAlign = TextAlign.Center,
                    text = "Info",
                    style = MaterialTheme.typography.h5
                )
                Text(
                    textAlign = TextAlign.Center,
                    text = "Info",
                    style = MaterialTheme.typography.h5
                )
                Text(
                    textAlign = TextAlign.Center,
                    text = "Info",
                    style = MaterialTheme.typography.h5
                )
            }
        }
    }
}

@Preview
@Preview("Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSignUpScreen() {
    val authStatus: AuthState = rememberAuthState(
        email = "a@a.com",
        emailFocus = false,
        password = "aaaaaaaa",
        passwordFocus = false
    )
    SaveTheFoodTheme {
        SignUpScreen(
            email = object : LoginViewModel.LoginStatus("test") {
                override val checkStatus: (String) -> LoginStateValue = {
                    LoginStateValue.NONE
                }
            },
            onBack = {}
        ) {}
    }
}