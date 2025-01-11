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

class OtpAccountVerifyViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private var email = ""
    private var _otpUiState =
        MutableStateFlow(
            OtpUi(
                otp = "",
                navigateScreen = false,
                isLoading = false,
                error = null
            )
        )
    val otpUiState = _otpUiState.asStateFlow()

    fun verifyAccount() {
        if (_otpUiState.value.otp.length < 4) {
            _otpUiState.value = _otpUiState.value.copy(isLoading = false, error = "Invalid OTP")
            return
        }
        viewModelScope.launch {
            _otpUiState.value = _otpUiState.value.copy(isLoading = true, error = null)
            val otpDetails = _otpUiState.value.toOtpDetails(email = email)
            when (val result = authRepository.accountVerification(otpDetails)) {
                is Result.Success -> {
                    _otpUiState.value = _otpUiState.value.copy(
                        navigateScreen = true,
                        isLoading = false,
                        error = null
                    )
                    println(result.message)
                }

                is Result.Error -> {
                    _otpUiState.value =
                        _otpUiState.value.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    fun resendOtp() {
        viewModelScope.launch {
            _otpUiState.value = _otpUiState.value.copy(isLoading = true, error = null)
            when (val result = authRepository.resendOtp(
                ResendOtpDetails(
                    email = email,
                    type = ResendOtpType.ACCOUNT_VERIFICATION
                )
            )) {
                is Result.Success -> {
                    _otpUiState.value = _otpUiState.value.copy(
                        isLoading = false,
                        error = null
                    )
                }

                is Result.Error -> {
                    _otpUiState.value =
                        _otpUiState.value.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    fun onOtpChange(otp: String) {
        if (otp.length <= 4) {
            _otpUiState.value = _otpUiState.value.copy(otp = otp)
        }
    }

    fun initEmail(email: String) {
        this.email = email
    }

    fun resetNavigate() {
        _otpUiState.value =
            _otpUiState.value.copy(otp = "", navigateScreen = false, isLoading = false)
    }
}