package org.example.project.kmmchat.presentation.conversations

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.kmmchat.domain.model.MessageResponse
import org.example.project.kmmchat.presentation.ContentType

data class MessageResponseUI(
    val id: String,
    val content: String,
    val contentType: ContentType,
    val createdAt: String,
    val isMine: Boolean,
    val senderId: String,
    val senderName: String
)

fun MessageResponse.toMessageResponseUI(): MessageResponseUI{
    return MessageResponseUI(
        id = id,
        content = content,
        contentType = if(contentType == "text") ContentType.TEXT else ContentType.IMAGE,
        createdAt = createdAt.convertToDate(),
        senderId = senderId,
        senderName = senderName,
        isMine = isMine
    )
}

fun String.convertToDate(): String{
    return try{
        val instant = Instant.parse(this)
        val localTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val hours = when(localTime.hour){
            0 -> 12
            in 12..23 -> localTime.hour - 12
            else -> localTime.hour
        }
        val minutes = localTime.minute.toString().padStart(2, '0')
        val amPm = if(localTime.hour < 12 ) "AM" else "PM"
        return "$hours:$minutes, $amPm"
    }
    catch(e: Exception){
        this
    }
}