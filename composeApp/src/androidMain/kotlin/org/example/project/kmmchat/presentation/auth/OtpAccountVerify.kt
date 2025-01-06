package org.example.project.kmmchat.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun OtpAccountVerify(
    email: String,
    navigateSignIn: () -> Unit,
    navigateBack: () -> Unit,
    viewModel: OtpAccountVerifyViewModel = koinViewModel()
) {
    val otpUiState by viewModel.otpUiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        // not used savedStateHandle to make viewModel compatible with IOS
        viewModel.initEmail(email = email)
    }
    LaunchedEffect(key1 = otpUiState) {
        if (otpUiState.navigateScreen) {
            navigateSignIn()
            viewModel.resetNavigate()
        }
    }
    Otp(
        title = "OTP For Account Verification",
        email = email,
        otpUiState = otpUiState,
        navigateBack = navigateBack,
        onChangeOtp = { viewModel.onOtpChange(it) },
        onSubmit = { viewModel.verifyAccount() },
        resendOtp = { viewModel.resendOtp() }
    )
}