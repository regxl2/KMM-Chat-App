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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.example.project.kmmchat.presentation.NavRoutes
import org.example.project.kmmchat.presentation.common.CircularIndicatorBox
import org.example.project.kmmchat.presentation.common.LabelTextField
import org.example.project.kmmchat.presentation.common.LabelTextFieldType
import org.koin.androidx.compose.koinViewModel


@Composable
fun ForgotPasswordRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ForgotPasswordViewModel = koinViewModel()
) {
    val forgotPasswordUiState by viewModel.forgotPasswordUiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = forgotPasswordUiState) {
        if(forgotPasswordUiState.navigateToOtp){
            navController.navigate(NavRoutes.OtpPassVerify(email = forgotPasswordUiState.email))
            viewModel.resetNavigate()
        }
    }
    ForgotPassword(
        modifier = modifier,
        email = forgotPasswordUiState.email,
        isLoading = forgotPasswordUiState.isLoading,
        error = forgotPasswordUiState.error,
        onEmailChange = { email -> viewModel.onEmailChange(email) },
        onSubmit = { viewModel.onSubmit() },
        navigateSignIn = { navController.popBackStack() }
    )
}

@Composable
fun ForgotPassword(
    modifier: Modifier = Modifier,
    email: String,
    isLoading: Boolean,
    error: String?,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
    navigateSignIn: () -> Unit
) {
    val emailFocusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        emailFocusRequester.requestFocus()
    }

    CircularIndicatorBox(isLoading = isLoading) {
        Column(
            modifier = modifier
                .padding(
                    16.dp
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Forgot Password",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            LabelTextField(
                label = "Email",
                value = email,
                onValueChange = onEmailChange,
                type = LabelTextFieldType.EMAIL,
                focusRequester = emailFocusRequester,
                changeFocus = { focusManager.clearFocus() }
            )
            error?.let {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(modifier = Modifier.align(Alignment.Center), text = it, color = Color.Red)
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSubmit,
                enabled = email.isNotEmpty() && !isLoading
            ) {
                Text(text = "Submit")
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable(onClick = navigateSignIn),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    text = "Back to Sign In",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ForgotPassword(
        email = "example@gmail.com",
        isLoading = false,
        error = null,
        onEmailChange = {},
        onSubmit = {},
        navigateSignIn = {}
    )
}