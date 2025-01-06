package org.example.project.kmmchat.data.repository

import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.example.project.kmmchat.data.remote.auth_data_source.dto.ResponseDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.toResponse
import org.example.project.kmmchat.data.remote.chat_data_source.ChatDataSource
import org.example.project.kmmchat.data.remote.chat_data_source.ChatResponseDto
import org.example.project.kmmchat.data.remote.common_dto.ErrorDto
import org.example.project.kmmchat.data.remote.common_dto.toMessageResponse
import org.example.project.kmmchat.data.remote.websocket_data_source.WebSocketDataSource
import org.example.project.kmmchat.domain.model.ChatRequest
import org.example.project.kmmchat.domain.model.MessageRequest
import org.example.project.kmmchat.domain.model.MessageResponse
import org.example.project.kmmchat.domain.model.Response
import org.example.project.kmmchat.util.Result
import org.example.project.kmmchat.domain.repository.ChatRepository
import org.example.project.kmmchat.platform.DispatcherProvider

class ChatRepositoryImpl(private val chatDataSource: ChatDataSource, private val webSocketDataSource: WebSocketDataSource): ChatRepository {
    override suspend fun getInitialChatRoom(chatRequest: ChatRequest): Result<List<MessageResponse>> {
        return withContext(DispatcherProvider.io){
            val httpResponse = chatDataSource.getInitialChatRoom(chatRequest = chatRequest)
            if(httpResponse.status.isSuccess()){
                val response = httpResponse.body<ChatResponseDto>().messages.map { it.toMessageResponse() }
                Result.Success(data = response)
            }
            else{
                val response = httpResponse.body<ErrorDto>()
                Result.Error(message = response.error)
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
            try{
                val result = chatDataSource.sendMessage(messageRequest)
                if (result.status.isSuccess()){
                    Result.Success(data = result.body<ResponseDto>().toResponse())
                }
                else{
                    Result.Error(message = result.body<ErrorDto>().error)
                }
            }
            catch (e: Exception){
                e.printStackTrace()
                Result.Error(
                    message = e.message
                )
            }
        }
    }

    override suspend fun disconnect() {
        withContext(DispatcherProvider.io){
            webSocketDataSource.disconnect()
        }
    }
}