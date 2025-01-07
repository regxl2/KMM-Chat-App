package org.example.project.kmmchat.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.example.project.kmmchat.domain.model.MessageRequest
import org.example.project.kmmchat.domain.repository.ChatRepository
import org.example.project.kmmchat.domain.usecase.GetTokenUseCase
import org.example.project.kmmchat.domain.usecase.GetUserIdUseCase
import org.example.project.kmmchat.util.ChatType
import org.example.project.kmmchat.util.ContentType
import org.example.project.kmmchat.presentation.conversations.toMessageResponseUI
import org.example.project.kmmchat.util.Result

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : ViewModel() {

    private val _chat =
        MutableStateFlow(ChatUi(conversationId = "", conversationType = ChatType.CHAT, name = ""))
    val chat: StateFlow<ChatUi> = _chat.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _text = MutableStateFlow("")
    val text = _text.asStateFlow()

    init {
        observeMessages()
        loadChatInformation()
    }

    fun initializeChat(conversationId: String, conversationType: ChatType, name: String) {
        _chat.value = _chat.value.copy(
            conversationId = conversationId,
            conversationType = conversationType,
            name = name
        )
    }

    fun onTextChange(newText: String) {
        _text.value = newText
    }

    fun sendMessage() {
        viewModelScope.launch {
            val token = getTokenUseCase().firstOrNull() ?: return@launch
            val result = chatRepository.sendMessage(
                messageRequest = MessageRequest(
                    token = token,
                    conversationId = chat.value.conversationId,
                    conversationType = chat.value.conversationType,
                    content = text.value,
                    contentType = ContentType.TEXT,
                )
            )
            when(result){
                is Result.Success -> {onTextChange("")}
                is Result.Error -> {}
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeMessages() {
        viewModelScope.launch {
            getUserIdUseCase().flatMapLatest { userId ->
                if (userId != null) {
                    chatRepository.getMessages(userId = userId).map { it.toMessageResponseUI() }
                } else {
                    flowOf()
                }
            }.collect { message ->
                _chat.value = _chat.value.copy(messages = _chat.value.messages + message)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadChatInformation() {
        viewModelScope.launch {
            _loading.value = true
            getTokenUseCase().flatMapLatest { token ->
                flow {
                    if (token == null) {
                        emit(Result.Error(message = "Something went wrong or try sign in again"))
                    } else {
                        val chatRequest = chat.value.toChatRequest(token = token)
                        emit(chatRepository.getInitialChatRoom(chatRequest = chatRequest))
                    }
                }
            }.collect { result ->
                _loading.value = false
                when (result) {
                    is Result.Error -> {
                        _error.value = result.message
                    }

                    is Result.Success -> {
                        if (result.data != null) {
                            _chat.value =
                                _chat.value.copy(messages = result.data.map { it.toMessageResponseUI() })
                        }
                    }
                }
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            chatRepository.disconnect()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}