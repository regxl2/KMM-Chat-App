package org.example.project.kmmchat.domain.model

data class ResendOtpDetails(
    val email: String,
    val type: ResendOtpType
)

enum class ResendOtpType{
    ACCOUNT_VERIFICATION,
    PASSWORD_RESET
}