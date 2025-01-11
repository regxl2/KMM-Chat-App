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
fun SignUp(
    modifier: Modifier = Modifier,
    navigateSignIn: () -> Unit,
    navigateAccountVerify: (String) -> Unit,
    viewModel: SignUpViewModel = koinViewModel()
) {
    val nameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val signUpUiState by viewModel.signUpUiState.collectAsStateWithLifecycle()
    val isSignUpEnabled by viewModel.isSignUpButtonEnabled.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        nameFocusRequester.requestFocus()
    }

    LaunchedEffect(key1 = signUpUiState) {
        if (signUpUiState.navigateToOtp) {
            navigateAccountVerify(signUpUiState.email)
            viewModel.resetNavigate()
        }
    }
    CircularIndicatorBox(isLoading = signUpUiState.isLoading) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Create Your Account",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Let's sign up to create your account and get started",
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyLarge
            )
            LabelTextField(
                label = "Name",
                value = signUpUiState.name,
                onValueChange = { viewModel.onNameChange(it) },
                type = LabelTextFieldType.NAME,
                focusRequester = nameFocusRequester,
                changeFocus = { emailFocusRequester.requestFocus() }
            )
            LabelTextField(
                label = "Email",
                value = signUpUiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                type = LabelTextFieldType.EMAIL,
                focusRequester = emailFocusRequester,
                changeFocus = { passwordFocusRequester.requestFocus() }
            )
            LabelTextField(
                label = "Password",
                value = signUpUiState.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                type = LabelTextFieldType.PASSWORD,
                focusRequester = passwordFocusRequester,
                changeFocus = { focusManager.clearFocus() }
            )
            signUpUiState.error?.let {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(modifier = Modifier.align(Alignment.Center), text = it, color = Color.Red, textAlign = TextAlign.Center)
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.signUp()
                },
                enabled = isSignUpEnabled
            ) {
                Text(
                    text = "Sign Up",
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Have an account?")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier.clickable(onClick = navigateSignIn),
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
private fun PreviewSignUp() {
    SignUp(navigateSignIn = {}, navigateAccountVerify = {})
}