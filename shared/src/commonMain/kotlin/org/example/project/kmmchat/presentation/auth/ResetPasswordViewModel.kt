package org.example.project.kmmchat.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.kmmchat.util.Result
import org.example.project.kmmchat.domain.usecase.ChangePasswordUseCase

class ResetPasswordViewModel(private val changePasswordUseCase: ChangePasswordUseCase) :
    ViewModel() {
    private var email: String = ""

    fun initEmail(email: String) {
        this.email = email
    }

    private val _resetPasswordUiState = MutableStateFlow(
        ResetPasswordUi(
            password = "",
            confirmPassword = "",
            isLoading = false,
            navigate = false,
            error = null
        )
    )
    val resetPasswordUiState = _resetPasswordUiState.asStateFlow()

    fun resetNavigate(){
        _resetPasswordUiState.value = ResetPasswordUi(
            password = "",
            confirmPassword = "",
            isLoading = false,
            navigate = false,
            error = null
        )
    }

    fun onChangePassword(password: String) {
        _resetPasswordUiState.value = _resetPasswordUiState.value.copy(password = password)
    }

    fun onChangeConfirmPassword(confirmPassword: String) {
        _resetPasswordUiState.value =
            _resetPasswordUiState.value.copy(confirmPassword = confirmPassword)
    }

    fun onClickSubmit() {
        if (_resetPasswordUiState.value.password != _resetPasswordUiState.value.confirmPassword) {
            _resetPasswordUiState.value =
                _resetPasswordUiState.value.copy(error = "Passwords are not matching")
            return
        }
        _resetPasswordUiState.value =
            _resetPasswordUiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val changePasswordDetails =
                    resetPasswordUiState.value.toChangePasswordDetails(email)
                when (val result = changePasswordUseCase(changePasswordDetails)) {
                    is Result.Success -> {
                        _resetPasswordUiState.value = _resetPasswordUiState.value.copy(
                            isLoading = false,
                            error = null,
                            navigate = true
                        )
                    }

                    is Result.Error -> {
                        _resetPasswordUiState.value = _resetPasswordUiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            } catch (e: Exception) {
                _resetPasswordUiState.value = _resetPasswordUiState.value.copy(
                    isLoading = false,
                    error = "An unexpected error occurred. Please try again."
                )
                println(e.message)
            }
        }
    }
}