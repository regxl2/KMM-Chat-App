package org.example.project.kmmchat.data.remote.chat_data_source.dto

import kotlinx.serialization.Serializable
import org.example.project.kmmchat.data.remote.common_dto.MessageResponseDto

@Serializable
data class ChatResponseDto(
    val messages: List<MessageResponseDto>
)