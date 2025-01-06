package org.example.project.kmmchat.presentation.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.kmmchat.presentation.common.CircularIndicatorBox
import org.example.project.kmmchat.presentation.common.LabelTextField
import org.example.project.kmmchat.presentation.common.LabelTextFieldType
import org.koin.androidx.compose.koinViewModel

@Composable
fun ResetPassword(
    modifier: Modifier = Modifier,
    email: String,
    navigateSignIn: () -> Unit,
    navigateOtpPassVerify: () -> Unit,
    viewModel: ResetPasswordViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.initEmail(email = email)
    }
    val resetPasswordUiState by viewModel.resetPasswordUiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = resetPasswordUiState) {
        if (resetPasswordUiState.navigate) {
            navigateOtpPassVerify()
            viewModel.resetNavigate()
        }
    }
    val pass1FocusRequester = remember { FocusRequester() }
    val pass2FocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    CircularIndicatorBox(isLoading = resetPasswordUiState.isLoading) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Set New Password",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Must be at least 8 characters",
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyLarge
            )
            LabelTextField(
                label = "Password",
                value = resetPasswordUiState.password,
                onValueChange = { viewModel.onChangePassword(password = it) },
                type = LabelTextFieldType.PASSWORD,
                focusRequester = pass1FocusRequester,
                changeFocus = { pass2FocusRequester.requestFocus() }
            )
            LabelTextField(
                label = "Confirm Password",
                value = resetPasswordUiState.confirmPassword,
                onValueChange = { viewModel.onChangeConfirmPassword(confirmPassword = it) },
                type = LabelTextFieldType.PASSWORD,
                focusRequester = pass2FocusRequester,
                changeFocus = { focusManager.clearFocus() }
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.onClickSubmit() }
            ) {
                Text(text = "Reset Password", color = MaterialTheme.colorScheme.onPrimary)
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable(onClick = navigateSignIn),
                    text = "Back to Sign In",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ResetPassword(
        email = "example@gmail.com",
        navigateSignIn = {},
        navigateOtpPassVerify = {}
    )
}