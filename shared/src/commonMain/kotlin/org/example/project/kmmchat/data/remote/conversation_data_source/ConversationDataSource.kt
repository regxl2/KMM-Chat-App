package org.example.project.kmmchat.data.remote.conversation_data_source

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.appendPathSegments

class ConversationDataSource(
    private val httpClient: HttpClient,
    apiUrl: String
) {
    private val chatUrl = "$apiUrl/chat"
    suspend fun getConversations(token: String): HttpResponse{
        return httpClient.get(urlString = chatUrl){
            url {
                appendPathSegments("get-conversations")
            }
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }
}