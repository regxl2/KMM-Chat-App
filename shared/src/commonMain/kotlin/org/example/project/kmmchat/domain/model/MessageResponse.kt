package org.example.project.kmmchat.domain.model


data class MessageResponse(
    val id: String,
    val content: String,
    val contentType: String,
    val createdAt: String,
    val isMine: Boolean,
    val senderId: String,
    val senderName: String
)