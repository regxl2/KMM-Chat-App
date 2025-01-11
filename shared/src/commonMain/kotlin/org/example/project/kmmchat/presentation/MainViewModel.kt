package org.example.project.kmmchat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.example.project.kmmchat.domain.repository.AuthRepository
import org.example.project.kmmchat.domain.repository.CredentialsRepository
import org.example.project.kmmchat.util.Destination
import org.example.project.kmmchat.util.Result

class MainViewModel(
    private val authRepository: AuthRepository,
    private val credentialsRepository: CredentialsRepository
) :
    ViewModel() {

    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val destination = credentialsRepository.getToken().flatMapLatest { value: String? ->
        flow {
            if (value == null) {
                emit(Destination.AUTH)
            } else {
                try {
                    val result = authRepository.authenticate(value)
                    emit(
                        when (result) {
                            is Result.Success -> {
                                credentialsRepository.setUserId(userId = result.data?.message)
                                _userId.value = result.data?.message.toString()
                                Destination.CONVERSATIONS
                            }

                            is Result.Error -> Destination.AUTH
                        }
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    emit(Destination.AUTH)
                }
            }
        }
    }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Destination.LOADING
    )

}