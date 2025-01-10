package org.example.project.kmmchat.domain.repository

import org.example.project.kmmchat.domain.model.SearchUsersDetails
import org.example.project.kmmchat.domain.model.Users
import org.example.project.kmmchat.util.Result

interface UserRepository {
    suspend fun searchUsers(searchUsersDetails: SearchUsersDetails): Result<Users>
    suspend fun searchForAddingRoomUsers(searchUsersDetails: SearchUsersDetails): Result<Users>
}