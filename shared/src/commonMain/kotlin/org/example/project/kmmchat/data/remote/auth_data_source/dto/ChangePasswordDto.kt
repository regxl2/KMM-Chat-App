package org.example.project.kmmchat.data.remote.auth_data_source.dto

import kotlinx.serialization.Serializable
import org.example.project.kmmchat.domain.model.ChangePasswordDetails

@Serializable
data class ChangePasswordDto(
    val email: String,
    val password: String
)

fun ChangePasswordDetails.toChangePasswordDto(): ChangePasswordDto {
    return ChangePasswordDto(
        email = email,
        password = password
    )
}
