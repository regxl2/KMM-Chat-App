package org.example.project.kmmchat.data.remote.chat_data_source

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import org.example.project.kmmchat.domain.model.ChatRequest
import org.example.project.kmmchat.domain.model.MessageRequest

class ChatDataSource(
    private val httpClient: HttpClient,
    apiUrl: String
) {
    private val chatUrl = "$apiUrl/chat"
    suspend fun getInitialChatRoom(chatRequest: ChatRequest): HttpResponse{
        return httpClient.get(urlString = chatUrl){
            url {
                appendPathSegments("get-chat")
            }
            headers {
                append(name = HttpHeaders.Authorization, value = "Bearer ${chatRequest.token}")
            }
            parameter("conversationId", chatRequest.conversationId)
            parameter("conversationType", chatRequest.conversationType)
        }
    }

    suspend fun sendMessage(messageRequest: MessageRequest): HttpResponse{
        val messageResponseDto = messageRequest.toMessageRequestDto()
        return httpClient.post(urlString = chatUrl){
            url{
                appendPathSegments("send-message")
            }
            headers{
                append(name = HttpHeaders.Authorization, value = "Bearer ${messageRequest.token}")
            }
            contentType(ContentType.Application.Json)
            setBody(messageResponseDto)
        }
    }
}