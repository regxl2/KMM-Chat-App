package org.example.project.kmmchat.data.remote.auth_data_source.dto

import kotlinx.serialization.Serializable
import org.example.project.kmmchat.domain.model.ResendOtpDetails

@Serializable
data class ResendOtpDto(
    val email: String
)

fun ResendOtpDetails.toResendOtpDto(): ResendOtpDto{
    return ResendOtpDto(
        email = email
    )
}