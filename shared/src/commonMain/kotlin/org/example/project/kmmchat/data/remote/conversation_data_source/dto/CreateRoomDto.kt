package org.example.project.kmmchat.data.remote.conversation_data_source.dto

import kotlinx.serialization.Serializable
import org.example.project.kmmchat.domain.model.CreateRoom

@Serializable
data class CreateRoomDto(
    val roomName: String
)

fun CreateRoom.toCreateRoomDto(): CreateRoomDto{
    return CreateRoomDto(
        roomName = roomName
    )
}