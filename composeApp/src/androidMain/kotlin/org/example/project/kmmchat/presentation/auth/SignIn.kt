package org.example.project.kmmchat.presentation.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import org.example.project.kmmchat.presentation.common.CircularIndicatorBox
import org.example.project.kmmchat.presentation.common.LabelTextField
import org.example.project.kmmchat.presentation.common.LabelTextFieldType
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignIn(
    modifier: Modifier = Modifier,
    navigateConversations: () -> Unit,
    navigateSignUp: () -> Unit,
    navigateForgotPassword: () -> Unit,
    viewModel: SignInViewModel = koinViewModel()
) {
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val signInUiState by viewModel.signInUiState.collectAsStateWithLifecycle()
    val isSignInEnabled by viewModel.isSignInButtonEnabled.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        emailFocusRequester.requestFocus()
    }

    LaunchedEffect(key1 = signInUiState) {
        if (signInUiState.navigate) {
            navigateConversations()
        }
    }

    CircularIndicatorBox(isLoading = signInUiState.isLoading) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Sign In To Your Account",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Let's sign in to your account and get started", color = Color.DarkGray,
                style = MaterialTheme.typography.bodyLarge
            )
            LabelTextField(
                label = "Email",
                value = signInUiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                type = LabelTextFieldType.EMAIL,
                focusRequester = emailFocusRequester,
                changeFocus = { passwordFocusRequester.requestFocus() }
            )
            LabelTextField(
                label = "Password",
                value = signInUiState.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                type = LabelTextFieldType.PASSWORD,
                focusRequester = passwordFocusRequester,
                changeFocus = { focusManager.clearFocus() }
            )
            signInUiState.error?.let {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(modifier = Modifier.align(Alignment.Center), text = it, color = Color.Red)
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.signIn() },
                enabled = isSignInEnabled
            ) {
                Text(
                    text = "Sign In",
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Don't have an account?")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier.clickable(onClick = navigateSignUp),
                    color = MaterialTheme.colorScheme.primary,
                    text = "Sign Up", style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable(onClick = navigateForgotPassword),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    text = "Forgot Password",
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
    SignIn(navigateConversations = {}, navigateSignUp = {}, navigateForgotPassword = {})
}