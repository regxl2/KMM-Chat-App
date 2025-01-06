package org.example.project.kmmchat.data.remote.auth_data_source.dto

import kotlinx.serialization.Serializable
import org.example.project.kmmchat.domain.model.OtpDetails

@Serializable
data class AccountVerificationDto(
    val email:String,
    val code:String
)

fun OtpDetails.toAccountVerificationDto(): AccountVerificationDto {
    return AccountVerificationDto(
        email = email,
        code = otp
    )
}
