package com.example.savethefood.ui.compose.auth

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.savethefood.R
import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.viewmodel.LoginViewModel
import com.example.savethefood.ui.compose.SaveTheFoodScaffold
import com.example.savethefood.ui.compose.component.BasicVerticalSurface
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import com.google.accompanist.insets.imePadding
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import org.koin.androidx.compose.getViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onUserLogged: (UserDomain?) -> Unit,
    onBack: () -> Unit,
    viewModel: LoginViewModel = getViewModel()
) {
    SaveTheFoodScaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colors.primarySurface,
                elevation = 4.dp,
                modifier = modifier
            ) {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    },
                    actions = {  },
                    backgroundColor = Color.Transparent,
                    contentColor = contentColorFor(MaterialTheme.colors.primarySurface),
                    elevation = 0.dp,
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding(bottom = false)
                )
            }
        }
    ) {
        /*BasicVerticalSurface{
            Image(
                modifier = Modifier.size(100.dp),
                alignment = Alignment.TopCenter,
                painter = painterResource(id = R.drawable.ic_food),
                contentDescription = "Logo"
            )
            Spacer(modifier = Modifier.height(170.dp))
        }*/
    }
}

@Preview
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSignUpScreen() {
    SaveTheFoodTheme {
        SignUpScreen(
            onUserLogged = {},
            onBack = {}
        )
    }
}