package org.example.project.kmmchat.presentation.auth

data class ForgotPasswordUI(
    val email: String,
    val isLoading: Boolean,
    val error: String?,
    val navigateToOtp: Boolean
)
