package org.example.project.kmmchat.presentation.conversations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.example.project.kmmchat.domain.repository.ConversationRepository
import org.example.project.kmmchat.domain.usecase.GetTokenUseCase
import org.example.project.kmmchat.domain.usecase.GetUserIdUseCase
import org.example.project.kmmchat.util.Result

class ConversationsViewModel(
    private val getTokenUseCase: GetTokenUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val conversationRepository: ConversationRepository
) : ViewModel() {

    private val _conversationUiState = MutableStateFlow(ConversationsUI())
    val conversationUiState = _conversationUiState.asStateFlow()

    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()


    init {
        getConversationList()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getConversationList() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            combine(getUserIdUseCase(), getTokenUseCase()) { userId, token ->
                Pair(userId, token)
            }
                .flatMapLatest { (userId, token) ->
                    if (userId == null || token == null) {
                        flow { emit(ConversationsUI()) }
                    } else {
                        flow {
                            _userId.value = userId
                            when (val result =
                                conversationRepository.getConversations(token)) {
                                is Result.Success -> {
                                    val conversations =
                                        result.data?.conversations?.map { it.toConversationUI() }
                                    if (conversations != null) {
                                        emit(ConversationsUI(conversations = conversations))
                                    } else {
                                        emit(ConversationsUI())
                                        _error.value = "No Conversation Found"
                                    }
                                }

                                is Result.Error -> {
                                    emit(ConversationsUI())
                                    _error.value = result.message
                                }
                            }
                        }
                    }
                }
                .filter { it.conversations.isNotEmpty() }
                .collect { newConversationUiState ->
                    _conversationUiState.value = newConversationUiState
                    _isLoading.value = false
                }
            _isLoading.value = false
        }
    }
}