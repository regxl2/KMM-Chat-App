package org.example.project.kmmchat.presentation.auth

import org.example.project.kmmchat.domain.model.ChangePasswordDetails

data class ResetPasswordUi(
    val password: String,
    val confirmPassword: String,
    val isLoading: Boolean,
    val navigate: Boolean,
    val error: String?
)

fun ResetPasswordUi.toChangePasswordDetails(email: String): ChangePasswordDetails{
    return ChangePasswordDetails(
        email = email,
        password = password
    )
}
