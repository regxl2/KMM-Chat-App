package org.example.project.kmmchat.data.repository

import org.example.project.kmmchat.data.remote.user_data_source.UserDataSource
import org.example.project.kmmchat.data.remote.user_data_source.dto.toUsers
import org.example.project.kmmchat.domain.model.Users
import org.example.project.kmmchat.domain.repository.NewConversationRepository
import org.example.project.kmmchat.util.Result

class NewConversationRepositoryImpl(private val userDataSource: UserDataSource): NewConversationRepository {
    override suspend fun searchUsers(query: String): Result<Users> {
        return when(val result = userDataSource.searchUsers(query = query)){
            is Result.Success -> Result.Success(data = result.data?.toUsers())
            is Result.Error -> Result.Error(message = result.message)
        }
    }
}