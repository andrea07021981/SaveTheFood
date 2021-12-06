package com.example.savethefood.ui.compose.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.savethefood.R
import com.example.savethefood.ui.theme.AlphaHalf
import com.example.savethefood.ui.theme.SaveTheFoodTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BasicInputTextfield(
    modifier: Modifier = Modifier,
    label: String,
    res: Int,
    isPasswordField: Boolean = false,
    passwordVisibility: Boolean = true,
    onPasswordVisibilityChanged: (Boolean) -> Unit = {},
    text: String,
    isError: Boolean = false,
    errorMessage: String = "",
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onFocusChanged: (FocusState) -> Unit = {},
    onTextChanged: (String) -> Unit,
    onImeAction: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Column {
        OutlinedTextField(
            modifier = modifier.onFocusChanged { onFocusChanged(it) },
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
                if (isError) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "error",
                        tint = SaveTheFoodTheme.colors.warning
                    )
                } else {
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
                }
            },
            isError = isError,
            singleLine = singleLine,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = SaveTheFoodTheme.colors.textSecondary,
                focusedBorderColor = SaveTheFoodTheme.colors.textSecondary,
                unfocusedBorderColor = SaveTheFoodTheme.colors.textSecondary.copy(alpha = AlphaHalf),
                focusedLabelColor = SaveTheFoodTheme.colors.textSecondary,
                unfocusedLabelColor = SaveTheFoodTheme.colors.textSecondary,
                backgroundColor = Color.Transparent,
                cursorColor = SaveTheFoodTheme.colors.textLink,
                disabledLabelColor = SaveTheFoodTheme.colors.textLink,
                errorCursorColor = SaveTheFoodTheme.colors.warning,
                errorLabelColor = SaveTheFoodTheme.colors.warning,
                errorBorderColor = SaveTheFoodTheme.colors.warning
            ),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onImeAction()
                    keyboardController?.hide() // it closes the keyboard
                },
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
        )
        // Display the error message
        if (isError) {
            Text(
                text = errorMessage,
                color = SaveTheFoodTheme.colors.warning,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewUserInputTextfield() {
    SaveTheFoodTheme {
        BasicInputTextfield(
            res = R.drawable.email_white_24dp,
            label = "User",
            text = "Preview",
            isError = true,
            errorMessage = "Wrong text",
            onTextChanged = {}
        )
    }
}