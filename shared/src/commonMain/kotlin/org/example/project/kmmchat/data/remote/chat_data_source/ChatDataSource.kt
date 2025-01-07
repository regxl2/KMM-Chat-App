package org.example.project.kmmchat.data.remote.chat_data_source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import org.example.project.kmmchat.data.remote.auth_data_source.dto.ResponseDto
import org.example.project.kmmchat.data.remote.common_dto.ErrorDto
import org.example.project.kmmchat.domain.model.ChatRequest
import org.example.project.kmmchat.domain.model.MessageRequest
import org.example.project.kmmchat.util.Result

class ChatDataSource(
    private val httpClient: HttpClient,
    apiUrl: String
) {
    private val chatUrl = "$apiUrl/chat"
    suspend fun getInitialChatRoom(chatRequest: ChatRequest): Result<ChatResponseDto>{
        return try{
            val httpResponse =  httpClient.get(urlString = chatUrl){
                url {
                    appendPathSegments("get-chat")
                }
                headers {
                    append(name = HttpHeaders.Authorization, value = "Bearer ${chatRequest.token}")
                }
                parameter("conversationId", chatRequest.conversationId)
                parameter("conversationType", chatRequest.conversationType)
            }
            if(httpResponse.status.isSuccess()){
                Result.Success(data = httpResponse.body<ChatResponseDto>())
            }
            else{
                Result.Error(message = httpResponse.body<ErrorDto>().error)
            }
        }
        catch (e: Exception){
            e.printStackTrace()
            Result.Error(message = e.message)
        }
    }

    suspend fun sendMessage(messageRequest: MessageRequest): Result<ResponseDto>{
        return try{
            val messageRequestDto = messageRequest.toMessageRequestDto()
            val httpResponse =  httpClient.post(urlString = chatUrl){
                url{
                    appendPathSegments("send-message")
                }
                headers{
                    append(name = HttpHeaders.Authorization, value = "Bearer ${messageRequest.token}")
                }
                contentType(ContentType.Application.Json)
                setBody(messageRequestDto)
            }
            if (httpResponse.status.isSuccess()){
                Result.Success(data = httpResponse.body<ResponseDto>())
            }
            else{
                Result.Error(message = httpResponse.body<ErrorDto>().error)
            }
        }
        catch(e: Exception){
            e.printStackTrace()
            Result.Error(message = e.message)
        }
    }
}