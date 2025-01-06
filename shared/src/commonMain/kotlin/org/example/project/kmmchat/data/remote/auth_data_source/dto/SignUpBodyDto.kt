package org.example.project.kmmchat.data.remote.auth_data_source.dto

import kotlinx.serialization.Serializable
import org.example.project.kmmchat.domain.model.SignUpBody

@Serializable
data class SignUpBodyDto(
    val name: String,
    val email: String,
    val password: String
)

fun SignUpBody.toSignUpDto(): SignUpBodyDto {
    return SignUpBodyDto(
        name = name,
        email = email,
        password = password
    )
}
