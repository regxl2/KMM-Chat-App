package org.example.project.kmmchat.data.remote.conversation_data_source.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.project.kmmchat.data.remote.common_dto.MessageResponseDto
import org.example.project.kmmchat.data.remote.common_dto.toMessageResponse
import org.example.project.kmmchat.domain.model.Conversation

@Serializable
data class ConversationDto(
    val conversationId: String,
    val conversationType: String,
    @SerialName("lastMessage")
    val messageResponseDto: MessageResponseDto?,
    val name: String
)

fun ConversationDto.toConversation(): Conversation{
    return Conversation(
        conversationId = conversationId,
        conversationType = conversationType,
        messageResponse = messageResponseDto?.toMessageResponse(),
        name = name
    )
}