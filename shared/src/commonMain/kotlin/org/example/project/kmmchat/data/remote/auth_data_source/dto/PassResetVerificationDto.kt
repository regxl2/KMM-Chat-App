package org.example.project.kmmchat.data.remote.auth_data_source.dto

import kotlinx.serialization.Serializable
import org.example.project.kmmchat.domain.model.OtpDetails

@Serializable
data class PassResetVerificationDto(
    val email: String,
    val code: String
)


fun OtpDetails.toPassResetVerificationDto(): PassResetVerificationDto {
    return PassResetVerificationDto(
        email = email,
        code = otp
    )
}
