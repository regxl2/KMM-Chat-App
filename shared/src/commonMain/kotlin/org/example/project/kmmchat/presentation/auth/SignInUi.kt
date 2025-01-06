package org.example.project.kmmchat.presentation.auth

import org.example.project.kmmchat.domain.model.SignInBody

data class SignInUi(
    val email: String,
    val password: String,
    val navigate: Boolean,
    val isLoading: Boolean,
    val error: String?
)

fun SignInUi.toSignInBody(): SignInBody{
    return SignInBody(
        email = email,
        password = password
    )
}