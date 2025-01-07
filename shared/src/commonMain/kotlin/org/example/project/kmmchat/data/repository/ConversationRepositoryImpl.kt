package org.example.project.kmmchat.data.repository

import kotlinx.coroutines.withContext
import org.example.project.kmmchat.data.remote.conversation_data_source.ConversationDataSource
import org.example.project.kmmchat.data.remote.conversation_data_source.dto.toConversations
import org.example.project.kmmchat.domain.model.Conversations
import org.example.project.kmmchat.domain.repository.ConversationRepository
import org.example.project.kmmchat.platform.DispatcherProvider
import org.example.project.kmmchat.util.Result

class ConversationRepositoryImpl(private  val conversationDataSource: ConversationDataSource): ConversationRepository {
    override suspend fun getConversations(token: String): Result<Conversations> {
        return withContext(DispatcherProvider.io){
            when(val response = conversationDataSource.getConversations(token = token)){
                is Result.Success -> Result.Success(data = response.data?.toConversations())
                is Result.Error -> Result.Error(message = response.message)
            }
        }
    }
}