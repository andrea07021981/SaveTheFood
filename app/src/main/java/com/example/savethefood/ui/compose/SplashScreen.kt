package com.example.savethefood.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.savethefood.R
import kotlinx.coroutines.delay

private const val SplashWaitTime: Long = 2000

/**
 * TODO migrate to SplashApi from https://developer.android.com/guide/topics/ui/splash-screen/migrate#best-practices
 * TODO do wee need to keep it and check the current api version >30 in the SaveTheFoodApp?
 */
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onTimeOut: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val currentTimeOut by rememberUpdatedState(newValue = onTimeOut)
        LaunchedEffect(true) {
            delay(SplashWaitTime) // simulate some setting operations
            currentTimeOut()
        }

        Image(painter = painterResource(id = R.drawable.ic_food), contentDescription = "Logo")
    }
}

@Preview
@Composable
fun PreviewSplashScreen() {
    SplashScreen(onTimeOut = {})
}