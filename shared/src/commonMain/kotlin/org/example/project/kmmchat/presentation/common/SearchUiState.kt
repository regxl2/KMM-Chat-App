package org.example.project.kmmchat.presentation.common

import org.example.project.kmmchat.domain.model.User

sealed class SearchUiState {
    data object Idle: SearchUiState()
    data object Loading: SearchUiState()
    data class Error(val message: String): SearchUiState()
    data class Result(val users: List<User>): SearchUiState()
}