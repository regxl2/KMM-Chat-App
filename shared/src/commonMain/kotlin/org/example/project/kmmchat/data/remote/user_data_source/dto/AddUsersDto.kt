package org.example.project.kmmchat.data.remote.user_data_source.dto

import kotlinx.serialization.Serializable
import org.example.project.kmmchat.domain.model.Users

@Serializable
data class AddUsersDto(
    val users: List<AddUserDto>
)

fun AddUsersDto.toUsers(): Users{
    return Users(
        users = users.map { it.toUser() }
    )
}
