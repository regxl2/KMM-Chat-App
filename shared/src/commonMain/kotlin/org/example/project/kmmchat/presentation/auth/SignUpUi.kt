package org.example.project.kmmchat.presentation.auth

import org.example.project.kmmchat.domain.model.SignUpBody

data class SignUpUi(
    val name: String,
    val email: String,
    val password: String,
    val isLoading: Boolean,
    val error: String?,
    val navigateToOtp: Boolean
)

fun SignUpUi.toSignUpBody(): SignUpBody{
    return SignUpBody(
        name = name,
        email = email,
        password = password
    )
}
