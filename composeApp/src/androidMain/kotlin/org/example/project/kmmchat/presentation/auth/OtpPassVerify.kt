package org.example.project.kmmchat.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun OtpPassVerify(
    email: String,
    navigateResetPassword: () -> Unit,
    navigateBack: () -> Unit,
    viewModel: OtpPassVerifyViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.initEmail(email = email)
    }
    val otpUiState by viewModel.otpPassVerifyUiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = otpUiState) {
        if(otpUiState.navigateScreen){
            navigateResetPassword()
            viewModel.resetNavigate()
        }
    }
    Otp(
        title = "OTP For Password Reset",
        email = email,
        otpUiState = otpUiState,
        navigateBack = navigateBack,
        onChangeOtp = { otp -> viewModel.onOtpChange(otp) },
        onSubmit = { viewModel.verifyPassReset() },
        resendOtp = { viewModel.resendOtp() }
    )
}