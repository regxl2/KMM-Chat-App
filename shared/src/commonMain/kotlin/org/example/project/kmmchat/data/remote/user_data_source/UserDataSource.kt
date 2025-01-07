package org.example.project.kmmchat.data.remote.user_data_source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import org.example.project.kmmchat.data.remote.common_dto.ErrorDto
import org.example.project.kmmchat.data.remote.user_data_source.dto.UsersDto
import org.example.project.kmmchat.util.Result

class UserDataSource(
    private val httpClient: HttpClient,
    url: String
) {
    private val apiUrl = "$url/user"

    suspend fun searchUsers(query: String): Result<UsersDto>{
        return try{
            val response = httpClient.get(urlString = apiUrl){
                url {
                    appendPathSegments("search")
                }
                parameters{
                    append("query", query)
                }
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

}