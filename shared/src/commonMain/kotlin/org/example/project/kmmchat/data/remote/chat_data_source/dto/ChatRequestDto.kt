package org.example.project.kmmchat.data.remote.chat_data_source.dto

import org.example.project.kmmchat.domain.model.ChatRequest

data class ChatRequestDto(
    val conversationId: String,
    val conversationType: String
)

fun ChatRequest.toChatRequestDto(): ChatRequestDto {
    return ChatRequestDto(
        conversationId = conversationId,
        conversationType = conversationType
    )
}
