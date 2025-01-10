package org.example.project.kmmchat.data.remote.conversation_data_source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import org.example.project.kmmchat.data.remote.common_dto.ResponseDto
import org.example.project.kmmchat.data.remote.common_dto.ErrorDto
import org.example.project.kmmchat.data.remote.conversation_data_source.dto.ConversationsDto
import org.example.project.kmmchat.data.remote.conversation_data_source.dto.CreateRoomDto
import org.example.project.kmmchat.util.Result

class ConversationDataSource(
    private val httpClient: HttpClient,
    apiUrl: String
) {
    private val chatUrl = "$apiUrl/chat"
    suspend fun getConversations(token: String): Result<ConversationsDto>{
        return try{
            val httpResponse = httpClient.get(urlString = chatUrl){
                url {
                    appendPathSegments("get-conversations")
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            if(httpResponse.status.isSuccess()){
                Result.Success(data = httpResponse.body<ConversationsDto>())
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

    suspend fun createRoom(token: String, createRoomDto: CreateRoomDto): Result<ResponseDto>{
        return try{
            val httpResponse = httpClient.post(urlString = chatUrl){
                url {
                    appendPathSegments("create-room")
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
                contentType(ContentType.Application.Json)
                setBody(createRoomDto)
            }
            if(httpResponse.status.isSuccess()){
                Result.Success(data = httpResponse.body<ResponseDto>())
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

}