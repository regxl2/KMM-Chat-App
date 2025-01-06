package org.example.project.kmmchat.domain.model

data class Conversation(
    val conversationId: String,
    val conversationType: String,
    val messageResponse: MessageResponse?,
    val name: String
)