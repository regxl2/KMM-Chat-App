package org.example.project.kmmchat.domain.repository

import org.example.project.kmmchat.domain.model.Users
import org.example.project.kmmchat.util.Result

interface NewConversationRepository {
    suspend fun searchUsers(query: String): Result<Users>
}