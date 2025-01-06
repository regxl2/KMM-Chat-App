package org.example.project.kmmchat.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.example.project.kmmchat.util.Result
import org.example.project.kmmchat.domain.usecase.SetTokenUseCase
import org.example.project.kmmchat.domain.usecase.SignInUseCase

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val setTokenUseCase: SetTokenUseCase
) : ViewModel() {
    private var _signInUiState = MutableStateFlow(
        SignInUi(
            email = "",
            password = "",
            navigate = false,
            isLoading = false,
            error = null
        )
    )
    val signInUiState = _signInUiState.asStateFlow()

    val isSignInButtonEnabled = _signInUiState.map {
        it.email.isNotEmpty() && it.password.isNotEmpty() && !it.isLoading
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun signIn() {
        _signInUiState.value = _signInUiState.value.copy(isLoading = true, error = null)
        val signInBody = _signInUiState.value.toSignInBody()
        viewModelScope.launch {
            try {
                when (val result = signInUseCase(signInBody = signInBody)) {
                    is Result.Success -> {
                       if(result.data == null) throw NoSuchElementException()
                        setTokenUseCase(result.data.token)
                        println(result.data.token)
                        _signInUiState.value = _signInUiState.value.copy(
                            navigate = true,
                            isLoading = false,
                            error = null
                        )
                    }

                    is Result.Error -> {
                        _signInUiState.value =
                            _signInUiState.value.copy(
                                navigate = false,
                                isLoading = false,
                                error = result.message
                            )
                    }
                }
            } catch (e: Exception) {
                _signInUiState.value = _signInUiState.value.copy(
                    isLoading = false,
                    error = "An unexpected error occurred. Please try again."
                )
                println(e.message)
            }
        }
    }


    fun resetNavigate() {
        _signInUiState.value = _signInUiState.value.copy(navigate = false, password = "")
    }

    fun onEmailChange(email: String) {
        _signInUiState.value = _signInUiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _signInUiState.value = _signInUiState.value.copy(password = password)
    }
}