package org.example.project.kmmchat.presentation.conversations

import org.example.project.kmmchat.domain.model.Conversation
import org.example.project.kmmchat.presentation.ChatType

data class ConversationUI(
    val conversationId: String,
    val conversationType: ChatType,
    val messageResponse: MessageResponseUI?,
    val name: String
)

fun Conversation.toConversationUI(): ConversationUI{
    return ConversationUI(
        conversationId = conversationId,
        conversationType = if(conversationType == "chat") ChatType.CHAT else ChatType.ROOM,
        messageResponse = messageResponse?.toMessageResponseUI(),
        name = name
    )
}