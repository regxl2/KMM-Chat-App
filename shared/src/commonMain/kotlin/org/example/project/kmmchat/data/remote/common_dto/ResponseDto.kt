package org.example.project.kmmchat.data.remote.common_dto

import kotlinx.serialization.Serializable
import org.example.project.kmmchat.domain.model.Response

@Serializable
data class ResponseDto(
    val message: String
)

fun ResponseDto.toResponse(): Response{
    return Response(
        message = message
    )
}
