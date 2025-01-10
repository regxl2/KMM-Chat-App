package org.example.project.kmmchat.data.remote.chat_data_source.dto

import kotlinx.serialization.Serializable
import org.example.project.kmmchat.domain.model.AddRoomUserDetails

@Serializable
data class AddRoomUserDetailsDto(
    val roomId: String,
    val userId: String
)

fun AddRoomUserDetails.toAddRoomUserDetailsDto(): AddRoomUserDetailsDto{
    return AddRoomUserDetailsDto(
        roomId = roomId,
        userId = userId
    )
}
