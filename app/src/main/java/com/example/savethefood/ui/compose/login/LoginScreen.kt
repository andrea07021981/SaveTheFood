package com.example.savethefood.ui.compose.login

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.savethefood.R
import com.example.savethefood.shared.data.domain.FoodDomain
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
    viewModel: LoginViewModel = getViewModel()
) {
    val context =  LocalContext.current
    // TODO temporary use of composable state. Remember does not work with the anonymous objects like username and password
    // TODO add a state holder for the login
    val (email, setEmail) = rememberSaveable {
        mutableStateOf("")
    }
    var emailHasFocus by remember { mutableStateOf(false)}
    val (psw, setPsw) = rememberSaveable {
        mutableStateOf("")
    }
    var pswHasFocus by remember { mutableStateOf(false)}

    var passwordVisibility by rememberSaveable { mutableStateOf(true) }
    val userEmail by remember { mutableStateOf(viewModel.email) }
    val userNameStatus by userEmail.valueStatus.observeAsState()
    val userPsw by remember { mutableStateOf(viewModel.password) }
    val userPswStatus by userPsw.valueStatus.observeAsState()

    // TODO temp until I remove all up with auth state
    val loginState = viewModel.loginAuthenticationState.observeAsState().value
    if (loginState is LoginAuthenticationStates.Authenticated) {
        onUserLogged(loginState.user)
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
                        emailHasFocus = it.run {
                            if (isFocused.not() && emailHasFocus) {
                                userEmail.value = email
                                userEmail.onFocusChanged(userEmail.value)
                            }
                            isFocused
                        }
                    },
                res = R.drawable.email_white_24dp,
                label = context.resources.getString(R.string.username),
                text = email,
                isError = userNameStatus?.hasLoginError() == true,
                errorMessage = userNameStatus?.message ?: "",
                imeAction = ImeAction.Next,
                onTextChanged = setEmail
            )
            Spacer(modifier = Modifier.height(16.dp))
            BasicInputTextfield(
                modifier = Modifier
                    .fillMaxWidth(.8F)
                    .onFocusChanged {
                        pswHasFocus = it.run {
                            if (it.isFocused.not() && pswHasFocus) {
                                userPsw.value = psw
                                userPsw.onFocusChanged(userPsw.value)
                            }
                            isFocused
                        }
                    },
                label = context.resources.getString(R.string.password),
                res = R.drawable.email_white_24dp,
                isPasswordField = true,
                passwordVisibility = passwordVisibility,
                onPasswordVisibilityChanged = {
                    passwordVisibility = !it
                },
                text = psw,
                isError = userPswStatus?.hasLoginError() == true,
                errorMessage = userPswStatus?.message ?: "",
                onTextChanged = setPsw,
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
fun PreviewPantryScreen() {
    SaveTheFoodTheme {
        LoginScreen(
            onUserLogged = {}
        )
    }
}