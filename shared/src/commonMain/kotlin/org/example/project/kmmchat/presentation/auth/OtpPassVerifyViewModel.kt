package org.example.project.kmmchat.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.kmmchat.domain.model.ResendOtpDetails
import org.example.project.kmmchat.domain.model.ResendOtpType
import org.example.project.kmmchat.util.Result
import org.example.project.kmmchat.domain.usecase.PassResetVerificationUseCase
import org.example.project.kmmchat.domain.usecase.ResendOtpUseCase

class OtpPassVerifyViewModel(
    private val resendOtpUseCase: ResendOtpUseCase,
    private val passResetVerificationUseCase: PassResetVerificationUseCase
) : ViewModel() {
    private var email = ""
    private val _otpPassVerifyUiState = MutableStateFlow(
        OtpUi(
            otp = "",
            isLoading = false,
            error = null,
            navigateScreen = false
        )
    )
    val otpPassVerifyUiState = _otpPassVerifyUiState.asStateFlow()

    fun initEmail(email: String) {
        this.email = email
    }

    fun onOtpChange(otp: String) {
        _otpPassVerifyUiState.value = _otpPassVerifyUiState.value.copy(otp = otp)
    }

    fun resetNavigate() {
        _otpPassVerifyUiState.value = OtpUi(
            otp = "",
            isLoading = false,
            error = null,
            navigateScreen = false
        )
    }

    fun verifyPassReset() {
        if (_otpPassVerifyUiState.value.otp.length < 4) {
            _otpPassVerifyUiState.value =
                _otpPassVerifyUiState.value.copy(isLoading = false, error = "Invalid OTP")
            return
        }
        _otpPassVerifyUiState.value =
            _otpPassVerifyUiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val otpDetails = _otpPassVerifyUiState.value.toOtpDetails(email = email)
                when (val result = passResetVerificationUseCase(otpDetails)) {
                    is Result.Success -> {
                        _otpPassVerifyUiState.value = _otpPassVerifyUiState.value.copy(
                            isLoading = false,
                            error = null,
                            navigateScreen = true
                        )
                    }

                    is Result.Error -> {
                        _otpPassVerifyUiState.value = _otpPassVerifyUiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            } catch (e: Exception) {
                _otpPassVerifyUiState.value = _otpPassVerifyUiState.value.copy(
                    isLoading = false,
                    error = "An unexpected error occurred. Please try again"
                )
                println(e.message)
            }
        }
    }

    fun resendOtp() {
        _otpPassVerifyUiState.value =
            _otpPassVerifyUiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                when (val result = resendOtpUseCase(
                    ResendOtpDetails(
                        email = email,
                        type = ResendOtpType.PASSWORD_RESET
                    )
                )) {
                    is Result.Success -> {
                        _otpPassVerifyUiState.value =
                            _otpPassVerifyUiState.value.copy(isLoading = false, error = null)
                    }

                    is Result.Error -> {
                        _otpPassVerifyUiState.value = _otpPassVerifyUiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            } catch (e: Exception) {
                _otpPassVerifyUiState.value = _otpPassVerifyUiState.value.copy(
                    isLoading = false,
                    error = "An unexpected error occurred. Please try again"
                )
                println(e.message)
            }
        }
    }
}