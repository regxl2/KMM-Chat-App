package org.example.project.kmmchat.domain.model

data class ChatRequest(
    val conversationId: String,
    val conversationType: String,
    val token: String
)
