package com.example.savethefood.ui.compose.auth

import android.content.res.Configuration
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.savethefood.R
import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.utils.LoginAuthenticationStates
import com.example.savethefood.shared.viewmodel.LoginViewModel
import com.example.savethefood.ui.compose.SaveTheFoodScaffold
import com.example.savethefood.ui.compose.component.BasicTopAppBar
import com.example.savethefood.ui.compose.component.BasicVerticalSurface
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import com.google.accompanist.insets.imePadding
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.systemBarsPadding
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
        onBack = onBack
    )
}

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
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
        }
    ) {
        var isAnimated by remember { mutableStateOf(false) }
        val transition = updateTransition(targetState = isAnimated, label = "transition")
        val rocketOffset by transition.animateOffset(transitionSpec = {
            if (this.targetState) {
                tween(5000, delayMillis = 1000) // launch duration

            } else {
                tween(5500, delayMillis = 1000) // land duration
            }
        }, label = "rocket offset") { animated ->
            if (animated) {
                Offset(0f, 100f)
            } else {
                Offset(0f, 400f)
            }
        }

        LaunchedEffect(Unit) {
            isAnimated = true
        }
        BasicVerticalSurface(
            modifier = modifier
        ){
            Text(
                modifier = modifier.offset(rocketOffset.x.dp, rocketOffset.y.dp),
                text = "Let's get started!",
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(170.dp))

        }
    }
}
@Preview
@Preview("Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSignUpScreen() {
    SaveTheFoodTheme {
        SignUpScreen(
            onBack = {}
        )
    }
}