package com.example.savethefood.ui.compose.component

import android.content.res.Configuration
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.savethefood.R
import com.example.savethefood.ui.theme.AlphaHalf
import com.example.savethefood.ui.theme.SaveTheFoodTheme

@Composable
fun UserInputTextfield(
    modifier: Modifier = Modifier,
    label: String,
    res: Int,
    isPasswordField: Boolean = false,
    passwordVisibility: Boolean = true,
    onPasswordVisibilityChanged: (Boolean) -> Unit = {},
    text: String,
    onTextChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = onTextChanged,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                painter = painterResource(id = res),
                contentDescription = null,// decorative element
                tint = SaveTheFoodTheme.colors.textSecondary
            )
        },
        trailingIcon = {
            if (isPasswordField) {
                val image = if (passwordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(
                    onClick = {
                    onPasswordVisibilityChanged(passwordVisibility)
                    }
                ) {
                    Icon(
                        imageVector  = image,
                        contentDescription = "Password hide/show",
                        tint = SaveTheFoodTheme.colors.textSecondary
                    )
                }
            }
        },
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = SaveTheFoodTheme.colors.textSecondary,
            focusedBorderColor = SaveTheFoodTheme.colors.textSecondary,
            unfocusedBorderColor = SaveTheFoodTheme.colors.textSecondary.copy(alpha = AlphaHalf),
            focusedLabelColor = SaveTheFoodTheme.colors.textSecondary,
            unfocusedLabelColor = SaveTheFoodTheme.colors.textSecondary,
            backgroundColor = Color.Transparent,
            cursorColor = SaveTheFoodTheme.colors.textLink,
            disabledLabelColor = SaveTheFoodTheme.colors.textLink
        ),
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewUserInputTextfield() {
    SaveTheFoodTheme {
        UserInputTextfield(
            res = R.drawable.email_white_24dp,
            label = "User",
            text = "Preview",
            onTextChanged = {}
        )
    }
}