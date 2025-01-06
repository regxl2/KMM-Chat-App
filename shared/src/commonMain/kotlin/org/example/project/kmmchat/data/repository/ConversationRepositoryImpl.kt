package org.example.project.kmmchat.data.repository

import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.withContext
import org.example.project.kmmchat.data.remote.common_dto.ErrorDto
import org.example.project.kmmchat.data.remote.conversation_data_source.ConversationDataSource
import org.example.project.kmmchat.data.remote.conversation_data_source.dto.ConversationsDto
import org.example.project.kmmchat.data.remote.conversation_data_source.dto.toConversations
import org.example.project.kmmchat.domain.model.Conversations
import org.example.project.kmmchat.util.Result
import org.example.project.kmmchat.domain.repository.ConversationRepository
import org.example.project.kmmchat.platform.DispatcherProvider

class ConversationRepositoryImpl(private  val conversationDataSource: ConversationDataSource): ConversationRepository {
    override suspend fun getConversations(token: String): Result<Conversations> {
        return withContext(DispatcherProvider.io){
            val httpResponse = conversationDataSource.getConversations(token = token)
            if(httpResponse.status.isSuccess()){
                val success = httpResponse.body<ConversationsDto>().toConversations()
                Result.Success(data = success)
            }
            else{
                val error = httpResponse.body<ErrorDto>().error
                Result.Error(message = error)
            }
        }
    }
}