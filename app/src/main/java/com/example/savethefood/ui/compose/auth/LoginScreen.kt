package com.example.savethefood.ui.compose.auth

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.savethefood.R
import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.utils.LoginAuthenticationStates
import com.example.savethefood.shared.viewmodel.LoginViewModel
import com.example.savethefood.ui.compose.component.BasicButton
import com.example.savethefood.ui.compose.component.BasicInputTextfield
import com.example.savethefood.ui.compose.extention.hasLoginError
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import com.google.accompanist.insets.imePadding
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onUserLogged: (UserDomain?) -> Unit,
    authStatus: AuthState = rememberAuthState(
        email = "a@a.com",
        emailFocus = false,
        password = "aaaaaaaa",
        passwordFocus = false
    ),
    viewModel: LoginViewModel = getViewModel()
) {


    val userEmail by remember { mutableStateOf(viewModel.email) }
    val userNameStatus by userEmail.valueStatus.observeAsState()
    val userPsw by remember { mutableStateOf(viewModel.password) }
    val userPswStatus by userPsw.valueStatus.observeAsState()

    // TODO temp until I move all up with auth state
    val loginState = viewModel.loginAuthenticationState.observeAsState().value
    if (loginState is LoginAuthenticationStates.Authenticated) {
        onUserLogged(loginState.user)
        viewModel.resetState()
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .imePadding(), // This move the view up while the keyboard is opened
        shape = RectangleShape,
        color = SaveTheFoodTheme.colors.uiBackground,
        contentColor = SaveTheFoodTheme.colors.textSecondary,
        elevation = 20.dp,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
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
                                userEmail.value = authStatus.email
                                userEmail.onFocusChanged(userEmail.value)
                            }
                            isFocused
                        }
                    },
                res = R.drawable.email_white_24dp,
                label = authStatus.context.resources.getString(R.string.username),
                text = authStatus.email,
                isError = userNameStatus?.hasLoginError() == true,
                errorMessage = userNameStatus?.message ?: "",
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
                                userPsw.value = authStatus.password
                                userPsw.onFocusChanged(userPsw.value)
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
                isError = userPswStatus?.hasLoginError() == true,
                errorMessage = userPswStatus?.message ?: "",
                onTextChanged = authStatus.setPassword,
                onImeAction = viewModel::onSignInClick // Create a submit func that calls vm and clear the values?
            )
            Spacer(modifier = Modifier.height(64.dp))
            BasicButton(
                modifier = Modifier
                    .fillMaxWidth(.8F)
                    .height(60.dp),
                text = R.string.log_in,
                onClick = viewModel::onSignInClick
            )
            Spacer(modifier = Modifier.height(32.dp))
            BasicButton(
                modifier = Modifier
                    .fillMaxWidth(.8F)
                    .height(60.dp),
                text = R.string.sign_up,
                onClick = {
                    onUserLogged(null)
                }
            )
        }
    }
}


@Preview
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewLoginScreen() {
    SaveTheFoodTheme {
        LoginScreen(
            onUserLogged = {}
        )
    }
}