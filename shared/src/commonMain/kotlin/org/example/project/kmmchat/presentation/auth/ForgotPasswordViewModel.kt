package org.example.project.kmmchat.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.kmmchat.util.Result
import org.example.project.kmmchat.domain.usecase.ForgotPasswordRequestUseCase

class ForgotPasswordViewModel(private val forgotPasswordRequestUseCase: ForgotPasswordRequestUseCase) :
    ViewModel() {
    private var _forgotPasswordUiState = MutableStateFlow(
        ForgotPasswordUI(
            email = "",
            isLoading = false,
            error = null,
            navigateToOtp = false
        )
    )
    val forgotPasswordUiState = _forgotPasswordUiState.asStateFlow()

    fun onEmailChange(email: String) {
        _forgotPasswordUiState.value = _forgotPasswordUiState.value.copy(email = email)
    }

    fun resetNavigate() {
        _forgotPasswordUiState.value = _forgotPasswordUiState.value.copy(
            email = "",
            isLoading = false,
            error = null,
            navigateToOtp = false
        )
    }

    fun onSubmit() {
        _forgotPasswordUiState.value = _forgotPasswordUiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try{
                when(val result = forgotPasswordRequestUseCase(_forgotPasswordUiState.value.email)){
                    is Result.Success -> {
                        _forgotPasswordUiState.value = _forgotPasswordUiState.value.copy(
                            isLoading = false,
                            error = null,
                            navigateToOtp = true
                        )
                    }
                    is Result.Error -> {
                        _forgotPasswordUiState.value = _forgotPasswordUiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
            catch(e: Exception){
                _forgotPasswordUiState.value = _forgotPasswordUiState.value.copy(
                    isLoading = false,
                    error = "An unexpected error occurred. Please try again."
                )
                println(e.message)
            }
        }
    }
}