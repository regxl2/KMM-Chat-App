package org.example.project.kmmchat.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.example.project.kmmchat.domain.repository.AuthRepository
import org.example.project.kmmchat.util.Result

class SignUpViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private var _signUpUiState = MutableStateFlow(
        SignUpUi(
            name = "",
            email = "",
            password = "",
            isLoading = false,
            error = null,
            navigateToOtp = false
        )
    )
    val signUpUiState = _signUpUiState.asStateFlow()

    val isSignUpButtonEnabled = _signUpUiState.map {
        it.name.isNotEmpty() && it.email.isNotEmpty() && it.password.isNotEmpty() && !it.isLoading
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun signUp() {
        val signUpDetails = signUpUiState.value.toSignUpBody()
        _signUpUiState.value = _signUpUiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            when (val result = authRepository.signUp(signUpDetails)) {
                is Result.Success -> {
                    if (result.data != null) {
                        _signUpUiState.value = _signUpUiState.value.copy(
                            isLoading = false,
                            navigateToOtp = true
                        )
                    } else {
                        throw Exception()
                    }
                }
                is Result.Error -> {
                    _signUpUiState.value =
                        _signUpUiState.value.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    fun resetNavigate() {
        _signUpUiState.value = _signUpUiState.value.copy(navigateToOtp = false, password = "")
    }

    fun onNameChange(name: String) {
        _signUpUiState.value = _signUpUiState.value.copy(name = name)
    }

    fun onEmailChange(email: String) {
        _signUpUiState.value = _signUpUiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _signUpUiState.value = _signUpUiState.value.copy(password = password)
    }
}