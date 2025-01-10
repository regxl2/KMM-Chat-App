package org.example.project.kmmchat.domain.model

data class AddRoomUserDetails(
    val roomId: String,
    val userId: String,
    val token: String
)
