package org.example.project.kmmchat.domain.model

import org.example.project.kmmchat.util.ChatType
import org.example.project.kmmchat.util.ContentType

data class MessageRequest(
    val token: String,
    val conversationId: String,
    val conversationType: ChatType,
    val content: String,
    val contentType: ContentType
)