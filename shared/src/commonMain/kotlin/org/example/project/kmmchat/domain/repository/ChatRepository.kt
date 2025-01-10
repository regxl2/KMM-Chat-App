package org.example.project.kmmchat.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.kmmchat.domain.model.AddRoomUserDetails
import org.example.project.kmmchat.domain.model.ChatRequest
import org.example.project.kmmchat.domain.model.MessageRequest
import org.example.project.kmmchat.domain.model.MessageResponse
import org.example.project.kmmchat.domain.model.Response
import org.example.project.kmmchat.util.Result

interface ChatRepository {
    suspend fun getInitialChatRoom(chatRequest: ChatRequest): Result<List<MessageResponse>>
    suspend fun getMessages(userId: String): Flow<MessageResponse>
    suspend fun sendMessage(messageRequest: MessageRequest): Result<Response>
    suspend fun addUserToRoomChat(addRoomUserDetails: AddRoomUserDetails): Result<Response>
    suspend fun disconnect()
}