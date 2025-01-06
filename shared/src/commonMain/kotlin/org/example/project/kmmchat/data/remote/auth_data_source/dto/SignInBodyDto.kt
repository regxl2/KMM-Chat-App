package org.example.project.kmmchat.data.remote.auth_data_source.dto

import kotlinx.serialization.Serializable
import org.example.project.kmmchat.domain.model.SignInBody

@Serializable
data class SignInBodyDto(
    val email: String,
    val password: String
)

fun SignInBody.toSignInBodyDto(): SignInBodyDto {
    return SignInBodyDto(
        email = email,
        password = password
    )
}