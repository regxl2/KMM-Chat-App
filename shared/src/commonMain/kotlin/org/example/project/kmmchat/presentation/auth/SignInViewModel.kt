package org.example.project.kmmchat.presentation.auth

import dev.icerock.moko.mvvm.flow.cStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.kmmchat.domain.repository.AuthRepository
import org.example.project.kmmchat.domain.repository.CredentialsRepository
import org.example.project.kmmchat.util.Result

class SignInViewModel(
    private val authRepository: AuthRepository,
    private val credentialsRepository: CredentialsRepository
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
    val signInUiState = _signInUiState.asStateFlow().cStateFlow()

    fun signIn() {
        viewModelScope.launch {
            _signInUiState.value = _signInUiState.value.copy(isLoading = true, error = null)
            val signInBody = _signInUiState.value.toSignInBody()
            when (val result = authRepository.signIn(signInBody = signInBody)) {
                is Result.Success -> {
                    if (result.data == null) {
                        _signInUiState.value = _signInUiState.value.copy(
                            isLoading = false,
                            error = "Something went wrong, please try again"
                        )
                        return@launch
                    }
                    credentialsRepository.setToken(result.data.token)
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