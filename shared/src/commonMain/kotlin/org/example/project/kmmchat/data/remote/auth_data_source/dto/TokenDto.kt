package org.example.project.kmmchat.data.remote.auth_data_source.dto

import kotlinx.serialization.Serializable
import org.example.project.kmmchat.domain.model.Token

@Serializable
data class TokenDto(val token: String)

fun TokenDto.toToken(): Token{
    return Token(token = token)
}

