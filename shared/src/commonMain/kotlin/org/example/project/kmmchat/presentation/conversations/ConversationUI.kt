package org.example.project.kmmchat.presentation.conversations

import org.example.project.kmmchat.domain.model.Conversation
import org.example.project.kmmchat.presentation.common.MessageResponseUI
import org.example.project.kmmchat.presentation.common.toMessageResponseUIForConversationUi
import org.example.project.kmmchat.util.ChatType

data class ConversationUI(
    val conversationId: String,
    val conversationType: ChatType,
    val messageResponse: MessageResponseUI?,
    val name: String
)

fun Conversation.toConversationUI(userId: String): ConversationUI{
    return ConversationUI(
        conversationId = conversationId,
        conversationType = if(conversationType == "chat") ChatType.CHAT else ChatType.ROOM,
        messageResponse = messageResponse?.toMessageResponseUIForConversationUi(userId = userId),
        name = name
    )
}