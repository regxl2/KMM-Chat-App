package org.example.project.kmmchat.domain.model

import org.example.project.kmmchat.presentation.ChatType
import org.example.project.kmmchat.presentation.ContentType

data class MessageRequest(
    val token: String,
    val conversationId: String,
    val conversationType: ChatType,
    val content: String,
    val contentType: ContentType
)