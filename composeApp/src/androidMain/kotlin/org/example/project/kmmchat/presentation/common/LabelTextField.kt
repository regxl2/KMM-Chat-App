package org.example.project.kmmchat.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class LabelTextFieldType {
    EMAIL,
    PASSWORD,
    NAME,
}

@Composable
fun LabelTextField(
    label: String,
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    type: LabelTextFieldType,
    focusRequester: FocusRequester,
    changeFocus: () -> Unit
) {
    var isPassHidden by rememberSaveable {
        mutableStateOf(true)
    }
    val textFieldValue = TextFieldValue(
        text = value,
        selection = TextRange(value.length)
    )
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        when (type) {
            LabelTextFieldType.NAME -> {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = textFieldValue,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { changeFocus() }
                    ),
                    onValueChange = { onValueChange(it.text) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Face,
                            contentDescription = label
                        )
                    }
                )
            }

            LabelTextFieldType.EMAIL -> {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = textFieldValue,
                    onValueChange = { onValueChange(it.text) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { changeFocus() }
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = label
                        )
                    }
                )
            }

            LabelTextFieldType.PASSWORD -> {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = textFieldValue,
                    onValueChange = { onValueChange(it.text) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { changeFocus() }
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = label
                        )
                    },
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.clickable {
                                isPassHidden = !isPassHidden
                            },
                            imageVector = if (isPassHidden) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                            contentDescription = "Hide Password Icon"
                        )
                    },
                    visualTransformation = if (isPassHidden) PasswordVisualTransformation() else VisualTransformation.None
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    LabelTextField(
        label = "Email",
        value = "example@gmail.com",
        onValueChange = {},
        type = LabelTextFieldType.EMAIL,
        focusRequester = FocusRequester(),
        changeFocus = {}
    )
}