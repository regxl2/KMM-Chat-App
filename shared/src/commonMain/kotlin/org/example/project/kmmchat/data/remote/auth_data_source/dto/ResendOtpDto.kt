package org.example.project.kmmchat.data.remote.auth_data_source.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResendOtpDto(
    val email: String
)
