package org.example.project.kmmchat.data.remote.common_dto

import kotlinx.serialization.Serializable
import org.example.project.kmmchat.domain.model.MessageResponse

@Serializable
data class MessageResponseDto(
    val id: String,
    val content: String,
    val contentType: String,
    val createdAt: String,
    val isMine: Boolean,
    val senderId: String,
    val senderName: String
)

fun MessageResponseDto.toMessageResponse(): MessageResponse{
    return MessageResponse(
        id = id,
        content = content,
        contentType = contentType,
        createdAt = createdAt,
        senderId = senderId,
        senderName = senderName,
        isMine = isMine
    )
}
