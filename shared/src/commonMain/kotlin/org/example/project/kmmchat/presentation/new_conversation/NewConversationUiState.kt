package org.example.project.kmmchat.presentation.new_conversation

import org.example.project.kmmchat.domain.model.User

sealed class NewConversationUiState {
    data object Idle: NewConversationUiState()
    data object Loading: NewConversationUiState()
    data class Error(val message: String): NewConversationUiState()
    data class Result(val users: List<User>): NewConversationUiState()
}