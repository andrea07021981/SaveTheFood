package com.example.savethefood.ui.compose.auth

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.viewmodel.LoginViewModel
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onUserLogged: (UserDomain?) -> Unit,
    viewModel: LoginViewModel = getViewModel()
) {
}

@Preview
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSignUpScreen() {
    SaveTheFoodTheme {
        SignUpScreen(
            onUserLogged = {}
        )
    }
}