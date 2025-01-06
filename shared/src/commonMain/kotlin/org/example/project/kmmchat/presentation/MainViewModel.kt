package org.example.project.kmmchat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.example.project.kmmchat.domain.usecase.AuthenticateUseCase
import org.example.project.kmmchat.domain.usecase.GetTokenUseCase
import org.example.project.kmmchat.domain.usecase.SetUserIdUseCase
import org.example.project.kmmchat.util.Result

class MainViewModel(
    getTokenUseCase: GetTokenUseCase,
    setUserIdUseCase: SetUserIdUseCase,
    authenticateUseCase: AuthenticateUseCase
) :
    ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val destination = getTokenUseCase().flatMapLatest { value: String? ->
        flow {
            if (value == null) {
                emit(Destination.AUTH)
            } else {
                try {
                    val result = authenticateUseCase(value)
                    emit(
                        when (result) {
                            is Result.Success -> {
                                setUserIdUseCase(userId = result.data?.message)
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