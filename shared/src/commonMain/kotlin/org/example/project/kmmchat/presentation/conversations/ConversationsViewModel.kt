package org.example.project.kmmchat.presentation.conversations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
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


    fun getConversationList() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val userId = getUserIdUseCase().firstOrNull()
            val token = getTokenUseCase().firstOrNull()
            if (userId.isNullOrEmpty() || token.isNullOrEmpty()) {
                _isLoading.value = false
                _error.value = "Invalid credentials"
                return@launch
            }
            when (val result = conversationRepository.getConversations(token)) {
                is Result.Success -> {
                    if (result.data != null) {
                        println(result.data.conversations.size)
                        _conversationUiState.value =
                            ConversationsUI(conversations = result.data.conversations.map { it.toConversationUI() })
                    }
                }
                is Result.Error -> {
                    _error.value = result.message
                }
            }
            _isLoading.value = false
        }
    }
}