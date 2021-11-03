package com.example.savethefood.ui.compose.login

import android.content.res.Configuration
import android.util.Log
import android.widget.Button
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.savethefood.R
import com.example.savethefood.ui.compose.SaveTheFoodApp
import com.example.savethefood.ui.compose.component.UserInputTextfield
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import com.google.accompanist.insets.imePadding
import kotlinx.coroutines.launch

@Composable
fun Login(
    modifier: Modifier = Modifier
) {
    // TODO test, remove it and use the Viewmodel
    val (name, setName) = rememberSaveable {
        mutableStateOf("")
    }
    var passwordVisibility by rememberSaveable { mutableStateOf(true) }

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
            UserInputTextfield(
                modifier = Modifier.fillMaxWidth(.8F),
                res = R.drawable.email_white_24dp,
                label = "User",
                text = name,
                onTextChanged = setName
            )
            Spacer(modifier = Modifier.height(16.dp))
            UserInputTextfield(
                modifier = Modifier.fillMaxWidth(.8F),
                label = "Password",
                res = R.drawable.email_white_24dp,
                isPasswordField = true,
                passwordVisibility = passwordVisibility,
                onPasswordVisibilityChanged = {
                  passwordVisibility = !passwordVisibility
                },
                text = name,
                onTextChanged = setName
            )
            Spacer(modifier = Modifier.height(64.dp))
            Button(
                modifier = Modifier.fillMaxWidth(.8F).height(60.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = White,
                    contentColor = Black
                ),
                onClick = {
                    
                }) {
                    Text(
                        text = "Log In",
                        style = MaterialTheme.typography.button
                    )
            }
        }
    }
}


@Preview
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPantryScreen() {
    SaveTheFoodTheme {
        Login()
    }
}