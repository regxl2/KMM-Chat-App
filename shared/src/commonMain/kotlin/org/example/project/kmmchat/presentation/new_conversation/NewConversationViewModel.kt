package org.example.project.kmmchat.presentation.new_conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.example.project.kmmchat.domain.repository.NewConversationRepository
import org.example.project.kmmchat.domain.usecase.GetTokenUseCase
import org.example.project.kmmchat.domain.usecase.GetUserIdUseCase
import org.example.project.kmmchat.util.Result

class NewConversationViewModel(
    private val newConversationRepository: NewConversationRepository,
    private val tokenUseCase: GetTokenUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val newConversationUiState: StateFlow<NewConversationUiState> = query
        .debounce(1000)
        .filter { it.isNotEmpty() }
        .distinctUntilChanged()
        .flatMapLatest { text ->
            flow {
                emit(NewConversationUiState.Loading)
                val token = tokenUseCase().firstOrNull()
                if(token==null){
                    emit(NewConversationUiState.Error(message = "Invalid Token"))
                }
                else{
                    val result = newConversationRepository.searchUsers(query = text, token = token)
                    val state = when (result) {
                        is Result.Success -> {
                            if (result.data != null) {
                                NewConversationUiState.Result(users = result.data.users)
                            } else {
                                NewConversationUiState.Error(message = "No User Found")
                            }
                        }
                        is Result.Error -> NewConversationUiState.Error(
                            message = result.message ?: "Something Went Wrong"
                        )
                    }
                    emit(state)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NewConversationUiState.Idle
        )

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

}