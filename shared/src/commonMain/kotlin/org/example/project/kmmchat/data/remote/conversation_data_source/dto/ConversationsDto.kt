package org.example.project.kmmchat.data.remote.conversation_data_source.dto

import kotlinx.serialization.Serializable
import org.example.project.kmmchat.domain.model.Conversations

@Serializable
data class ConversationsDto(
    val conversations: List<ConversationDto>
)

fun ConversationsDto.toConversations(): Conversations{
    return Conversations(
        conversations = conversations.map { it.toConversation()}
    )
}