package org.example.project.kmmchat.data.repository

import org.example.project.kmmchat.data.remote.user_data_source.UserDataSource
import org.example.project.kmmchat.data.remote.user_data_source.dto.toUsers
import org.example.project.kmmchat.domain.model.SearchUsersDetails
import org.example.project.kmmchat.domain.model.Users
import org.example.project.kmmchat.domain.repository.UserRepository
import org.example.project.kmmchat.util.Result

class UserRepositoryImpl(private val userDataSource: UserDataSource) : UserRepository {
    override suspend fun searchUsers(searchUsersDetails: SearchUsersDetails): Result<Users> {
        return when (val result =
            userDataSource.searchUsers(query = searchUsersDetails.query, token = searchUsersDetails.token)) {
            is Result.Success -> Result.Success(data = result.data?.toUsers())
            is Result.Error -> Result.Error(message = result.message)
        }
    }

    override suspend fun searchForAddingRoomUsers(searchUsersDetails: SearchUsersDetails): Result<Users> {
        return when (val result = userDataSource.searchWithoutRoomUsers(
            conversationId = searchUsersDetails.conversationId!!,
            query = searchUsersDetails.query,
            token = searchUsersDetails.token
        )) {
            is Result.Success -> {
                Result.Success(data = result.data?.toUsers())
            }
            is Result.Error -> Result.Error(message = result.message)
        }
    }
}