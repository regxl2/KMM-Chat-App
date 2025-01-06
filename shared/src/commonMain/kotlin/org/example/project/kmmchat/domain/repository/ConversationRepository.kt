package org.example.project.kmmchat.domain.repository

import org.example.project.kmmchat.domain.model.Conversations
import org.example.project.kmmchat.util.Result

interface ConversationRepository {
    suspend fun getConversations(token: String): Result<Conversations>
}