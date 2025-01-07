package org.example.project.kmmchat.data.remote.chat_data_source

import kotlinx.serialization.Serializable
import org.example.project.kmmchat.domain.model.MessageRequest
import org.example.project.kmmchat.util.ChatType
import org.example.project.kmmchat.util.ContentType

@Serializable
data class MessageRequestDto(
    val conversationId: String,
    val conversationType: String,
    val content: String,
    val contentType: String
)


fun MessageRequest.toMessageRequestDto(): MessageRequestDto {
    return MessageRequestDto(
        conversationId = conversationId,
        conversationType = if(conversationType == ChatType.CHAT) "chat" else "room",
        content = content,
        contentType = if (contentType == ContentType.TEXT) "text" else "image"
    )
}