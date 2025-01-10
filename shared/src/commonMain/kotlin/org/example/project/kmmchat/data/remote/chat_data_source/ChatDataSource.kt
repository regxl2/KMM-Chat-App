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
import org.example.project.kmmchat.data.remote.chat_data_source.dto.AddRoomUserDetailsDto
import org.example.project.kmmchat.data.remote.chat_data_source.dto.ChatRequestDto
import org.example.project.kmmchat.data.remote.chat_data_source.dto.ChatResponseDto
import org.example.project.kmmchat.data.remote.chat_data_source.dto.MessageRequestDto
import org.example.project.kmmchat.data.remote.common_dto.ErrorDto
import org.example.project.kmmchat.data.remote.common_dto.ResponseDto
import org.example.project.kmmchat.util.Result

class ChatDataSource(
    private val httpClient: HttpClient,
    apiUrl: String
) {
    private val chatUrl = "$apiUrl/chat"
    suspend fun getInitialChatRoom(token: String, chatRequestDto: ChatRequestDto): Result<ChatResponseDto>{
        return try{
            val httpResponse =  httpClient.get(urlString = chatUrl){
                url {
                    appendPathSegments("get-chat")
                }
                headers {
                    append(name = HttpHeaders.Authorization, value = "Bearer $token")
                }
                parameter("conversationId", chatRequestDto.conversationId)
                parameter("conversationType", chatRequestDto.conversationType)
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

    suspend fun sendMessage(token: String, messageRequestDto: MessageRequestDto): Result<ResponseDto>{
        return try{
            val httpResponse =  httpClient.post(urlString = chatUrl){
                url{
                    appendPathSegments("send-message")
                }
                headers{
                    append(name = HttpHeaders.Authorization, value = "Bearer $token")
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

    suspend fun searchForAddingRoomUsers(token: String, addRoomUserDetailsDto: AddRoomUserDetailsDto): Result<ResponseDto>{
        return try{
            val httpResponse = httpClient.post(urlString = chatUrl){
                url{
                    appendPathSegments("add-room-user")
                }
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
                contentType(ContentType.Application.Json)
                setBody(addRoomUserDetailsDto)
            }
            if(httpResponse.status.isSuccess()){
                Result.Success(data = httpResponse.body<ResponseDto>())
            }
            else{
                Result.Error(message = httpResponse.body<ErrorDto>().error)
            }
        }
        catch (e: Exception){
            println("hello world")
            e.printStackTrace()
            Result.Error(message = e.message)
        }
    }
}