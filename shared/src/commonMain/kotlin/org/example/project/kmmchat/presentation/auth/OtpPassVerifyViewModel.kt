package org.example.project.kmmchat.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.kmmchat.domain.model.ResendOtpDetails
import org.example.project.kmmchat.domain.model.ResendOtpType
import org.example.project.kmmchat.domain.repository.AuthRepository
import org.example.project.kmmchat.util.Result

class OtpPassVerifyViewModel(
    private val authRepository: AuthRepository
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
        viewModelScope.launch {
            _otpPassVerifyUiState.value =
                _otpPassVerifyUiState.value.copy(isLoading = true, error = null)
            val otpDetails = _otpPassVerifyUiState.value.toOtpDetails(email = email)
            when (val result = authRepository.passResetVerification(otpDetails)) {
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
        }
    }

    fun resendOtp() {
        viewModelScope.launch {
            _otpPassVerifyUiState.value =
                _otpPassVerifyUiState.value.copy(isLoading = true, error = null)
            when (val result = authRepository.resendOtp(
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
        }
    }
}