package org.example.project.kmmchat.data.remote.user_data_source.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.project.kmmchat.domain.model.User

@Serializable
data class UserDto(
    @SerialName("_id")
    val id: String,
    val email: String,
    val name: String
)

fun UserDto.toUser(): User {
    return User(email = email, name = name)
}