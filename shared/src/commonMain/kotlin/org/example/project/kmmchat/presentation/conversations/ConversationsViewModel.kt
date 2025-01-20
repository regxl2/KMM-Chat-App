package org.example.project.kmmchat.presentation.conversations

import dev.icerock.moko.mvvm.flow.cStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.example.project.kmmchat.domain.repository.ConversationRepository
import org.example.project.kmmchat.domain.repository.CredentialsRepository
import org.example.project.kmmchat.util.Result

class ConversationsViewModel(
    private val credentialsRepository: CredentialsRepository,
    private val conversationRepository: ConversationRepository
) : ViewModel() {

    private val _conversationUiState = MutableStateFlow(ConversationsUI())
    val conversationUiState = _conversationUiState.asStateFlow().cStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow().cStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow().cStateFlow()

    private var job: Job? = null

    init {
        getConversationList()
    }


    fun logout(){
        viewModelScope.launch {
            credentialsRepository.setUserId(null)
            credentialsRepository.setToken(null)
        }
    }


    fun getConversationList() {
        job?.cancel()
        job = viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val userId = credentialsRepository.getUserId().firstOrNull()
            val token = credentialsRepository.getToken().firstOrNull()
            if (userId.isNullOrEmpty() || token.isNullOrEmpty()) {
                _isLoading.value = false
                _error.value = "Invalid credentials"
                return@launch
            }
            when (val result = conversationRepository.getConversations(token)) {
                is Result.Success -> {
                    if (result.data != null) {
                        _conversationUiState.value =
                            ConversationsUI(conversations = result.data.conversations.map { it.toConversationUI(userId) })
                    }
                }
                is Result.Error -> {
                    _error.value = result.message
                }
            }
            _isLoading.value = false
        }
    }

    fun clearStates(){
        _error.value = null
        _isLoading.value = false
        _conversationUiState.value = ConversationsUI()
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }
}