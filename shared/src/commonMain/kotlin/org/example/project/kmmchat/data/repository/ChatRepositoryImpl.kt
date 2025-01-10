package org.example.project.kmmchat.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.example.project.kmmchat.data.remote.common_dto.toResponse
import org.example.project.kmmchat.data.remote.chat_data_source.ChatDataSource
import org.example.project.kmmchat.data.remote.chat_data_source.dto.toAddRoomUserDetailsDto
import org.example.project.kmmchat.data.remote.chat_data_source.dto.toChatRequestDto
import org.example.project.kmmchat.data.remote.chat_data_source.dto.toMessageRequestDto
import org.example.project.kmmchat.data.remote.common_dto.toMessageResponse
import org.example.project.kmmchat.data.remote.websocket_data_source.WebSocketDataSource
import org.example.project.kmmchat.domain.model.AddRoomUserDetails
import org.example.project.kmmchat.domain.model.ChatRequest
import org.example.project.kmmchat.domain.model.MessageRequest
import org.example.project.kmmchat.domain.model.MessageResponse
import org.example.project.kmmchat.domain.model.Response
import org.example.project.kmmchat.domain.repository.ChatRepository
import org.example.project.kmmchat.platform.DispatcherProvider
import org.example.project.kmmchat.util.Result

class ChatRepositoryImpl(private val chatDataSource: ChatDataSource, private val webSocketDataSource: WebSocketDataSource): ChatRepository {
    override suspend fun getInitialChatRoom(chatRequest: ChatRequest): Result<List<MessageResponse>> {
        return withContext(DispatcherProvider.io){
            when(val response = chatDataSource.getInitialChatRoom(token = chatRequest.token, chatRequestDto = chatRequest.toChatRequestDto())){
                is Result.Success -> Result.Success(data = response.data?.messages?.map { it.toMessageResponse() })
                is Result.Error -> Result.Error(message = response.message)
            }
        }
    }

    override suspend fun getMessages(userId: String): Flow<MessageResponse> {
        return withContext(DispatcherProvider.io){
            webSocketDataSource.connect(userId = userId).map { it.toMessageResponse() }
        }
    }

    override suspend fun sendMessage(messageRequest: MessageRequest): Result<Response> {
        return withContext(DispatcherProvider.io){
            val messageRequestDto = messageRequest.toMessageRequestDto()
            when(val response = chatDataSource.sendMessage(token = messageRequest.token, messageRequestDto = messageRequestDto)){
                is Result.Success -> Result.Success(data = response.data?.toResponse())
                is Result.Error -> Result.Error(message = response.message)
            }
        }
    }

    override suspend fun addUserToRoomChat(addRoomUserDetails: AddRoomUserDetails): Result<Response> {
        return withContext(DispatcherProvider.io){
            val addRoomUserDto = addRoomUserDetails.toAddRoomUserDetailsDto()
            when(val response = chatDataSource.searchForAddingRoomUsers(token = addRoomUserDetails.token, addRoomUserDetailsDto = addRoomUserDto)){
                is Result.Success -> Result.Success(data = response.data?.toResponse())
                is Result.Error -> Result.Error(message = response.message)
            }
        }
    }

    override suspend fun disconnect() {
        withContext(DispatcherProvider.io){
            webSocketDataSource.disconnect()
        }
    }
}