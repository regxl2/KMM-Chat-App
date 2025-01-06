package org.example.project.kmmchat.presentation.auth

import org.example.project.kmmchat.domain.model.OtpDetails

data class OtpUi(
    val otp: String,
    val navigateScreen: Boolean,
    val isLoading: Boolean,
    val error: String?
)

fun OtpUi.toOtpDetails(email: String): OtpDetails{
    return OtpDetails(
        email = email,
        otp = otp
    )
}