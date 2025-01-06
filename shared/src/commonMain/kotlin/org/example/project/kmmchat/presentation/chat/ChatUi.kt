package org.example.project.kmmchat.presentation.chat

import org.example.project.kmmchat.domain.model.ChatRequest
import org.example.project.kmmchat.presentation.ChatType
import org.example.project.kmmchat.presentation.conversations.MessageResponseUI


data class ChatUi(
    val conversationId: String,
    val conversationType: ChatType,
    val name: String,
    val avatar: String? = null,
    val messages: List<MessageResponseUI> = emptyList()
)

fun ChatUi.toChatRequest(token: String): ChatRequest{
    return ChatRequest(
        conversationId = conversationId,
        conversationType = if(conversationType == ChatType.CHAT) "chat" else "room",
        token = token
    )
}
