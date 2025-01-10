package org.example.project.kmmchat.domain.model

data class SearchUsersDetails(
    val conversationId: String? = null,
    val query: String,
    val token: String
)
