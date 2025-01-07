package org.example.project.kmmchat.data.remote.user_data_source.dto

import kotlinx.serialization.Serializable
import org.example.project.kmmchat.domain.model.Users

@Serializable
data class UsersDto(
    val users: List<UserDto>
)

fun UsersDto.toUsers(): Users{
    return Users(
        users = users.map { it.toUser() }
    )
}