package org.example.project.kmmchat.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.example.project.kmmchat.di.OS
import org.example.project.kmmchat.domain.model.MessageRequest
import org.example.project.kmmchat.domain.model.WebSocketDetails
import org.example.project.kmmchat.domain.repository.ChatRepository
import org.example.project.kmmchat.domain.repository.CredentialsRepository
import org.example.project.kmmchat.presentation.common.toMessageResponseUIForChatUi
import org.example.project.kmmchat.util.ChatType
import org.example.project.kmmchat.util.ContentType
import org.example.project.kmmchat.util.Result

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val credentialsRepository: CredentialsRepository,
    private val os: OS
) : ViewModel() {

    private val _chat =
        MutableStateFlow(ChatUi(conversationId = "", conversationType = ChatType.CHAT, name = "", avatar = null, messages = emptyList()))
    val chat: StateFlow<ChatUi> = _chat.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _text = MutableStateFlow("")
    val text = _text.asStateFlow()

    private var getMessagesJob : Job? = null
    private var loadMessageJob : Job? = null

    init {
        if (os == OS.ANDROID){
            initStates()
        }
    }

    fun initStates(){
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
            val token = credentialsRepository.getToken().firstOrNull() ?: return@launch
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
                is Result.Success -> {
                    onTextChange("")
                }
                is Result.Error -> {}
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeMessages() {
        getMessagesJob?.cancel()
        getMessagesJob = viewModelScope.launch {
            credentialsRepository.getUserId().flatMapLatest { userId ->
                if (userId != null) {
                    val webSocketDetails = WebSocketDetails(userId = userId, conversationId = chat.value.conversationId)
                    chatRepository.getMessages(webSocketDetails).map { it.toMessageResponseUIForChatUi() }
                } else {
                    flowOf()
                }
            }.collect { message ->
                println(message.content)
                _chat.value = _chat.value.copy(messages = _chat.value.messages + message)
            }
        }
    }

    private fun loadChatInformation() {
        loadMessageJob?.cancel()
        loadMessageJob = viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val token = credentialsRepository.getToken().firstOrNull()
            if(token == null){
                _loading.value = false
                _error.value = "Invalid Credential"
                return@launch
            }
            val chatRequest = chat.value.toChatRequest(token = token)
            when (val result = chatRepository.getInitialChatRoom(chatRequest = chatRequest)) {
                is Result.Error -> {
                    _error.value = result.message
                }
                is Result.Success -> {
                    if (result.data != null) {
                        _chat.value = _chat.value.copy(messages = result.data.map { it.toMessageResponseUIForChatUi() })
                    }
                    else{
                        _error.value = "No messages"
                    }
                }
            }
            _loading.value = false
        }
    }

    fun clearStates() {
        _chat.value = ChatUi(conversationId = "", conversationType = ChatType.CHAT, name = "")
        _loading.value = false
        _error.value = null
        _text.value = ""
        disconnect()
    }

    private fun disconnect(){
        getMessagesJob?.cancel()
        getMessagesJob?.cancel()
        getMessagesJob = null
        loadMessageJob = null
        viewModelScope.launch {
            chatRepository.disconnect()
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            chatRepository.disconnect()
        }
        super.onCleared()
    }
}