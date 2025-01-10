package org.example.project.kmmchat.data.remote.user_data_source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import io.ktor.http.appendPathSegments
import io.ktor.http.isSuccess
import org.example.project.kmmchat.data.remote.common_dto.ErrorDto
import org.example.project.kmmchat.data.remote.user_data_source.dto.AddUsersDto
import org.example.project.kmmchat.data.remote.user_data_source.dto.UsersDto
import org.example.project.kmmchat.util.Result

class UserDataSource(
    private val httpClient: HttpClient,
    url: String
) {
    private val apiUrl = "$url/user"

    suspend fun searchUsers(query: String, token: String): Result<UsersDto>{
        return try{
            val response = httpClient.get(urlString = apiUrl){
                url {
                    appendPathSegments("search")
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
                parameter("query", query)
            }
            if(response.status.isSuccess()){
                Result.Success(data = response.body<UsersDto>())
            }
            else {
                Result.Error(message = response.body<ErrorDto>().error)
            }
        }
        catch (e: Exception){
            e.printStackTrace()
            Result.Error(message = e.message)
        }
    }

    suspend fun searchWithoutRoomUsers(conversationId:String, query: String, token: String): Result<AddUsersDto>{
        return try{
            val response = httpClient.get(urlString = apiUrl){
                url {
                    appendPathSegments("search-room-users")
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
                parameter("query", query)
                parameter("conversationId", conversationId)
            }
            if(response.status.isSuccess()){
                Result.Success(data = response.body<AddUsersDto>())
            }
            else {
                Result.Error(message = response.body<ErrorDto>().error)
            }
        }
        catch (e: Exception){
            e.printStackTrace()
            Result.Error(message = e.message)
        }
    }

}