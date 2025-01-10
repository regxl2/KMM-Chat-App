package org.example.project.kmmchat.domain.model

data class User(
    val email: String,
    val name: String,
    val isRoomMember: Boolean = false
)