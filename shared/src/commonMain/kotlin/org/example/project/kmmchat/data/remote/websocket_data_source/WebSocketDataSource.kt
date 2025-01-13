package org.example.project.kmmchat.data.remote.websocket_data_source

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.serialization.deserialize
import io.ktor.utils.io.errors.IOException
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.retryWhen
import org.example.project.kmmchat.data.remote.common_dto.MessageResponseDto

class WebSocketDataSource(
    private val httpClient: HttpClient, private val websocketUrl: String
) {

    private lateinit var webSocketSession: DefaultClientWebSocketSession

    companion object {
        const val TAG = "MessagesSocketDataSource"
        const val RETRY_DELAY = 3000L
        const val MAX_RETRIES = 5
    }

    fun connect(userId: String, conversationId: String): Flow<MessageResponseDto> {
        return flow {
            try {
                httpClient.webSocketSession {
                    url(websocketUrl)
                    parameter("userId", userId)
                    parameter("conversationId", conversationId)
                }
                    .also { webSocketSession = it }
                    .incoming.receiveAsFlow()
                    .collect { frame ->
                        try {
                            val message = webSocketSession.handleMessage(frame)
                            if (message != null) {
                                emit(message)
                            }
                        } catch (e: Exception) {
                            Logger.e(TAG) { "Error handling websocket frame" }
                        }
                    }
            } catch (e: Exception) {
                Logger.e(TAG) { "Error connecting to websocket" }
            }
        }.retryWhen { cause, attempt ->
            if (cause is IOException && attempt < MAX_RETRIES) {
                delay(RETRY_DELAY)
                true
            } else {
                false
            }
        }.catch {
            Logger.e(TAG) { "Error in Websocket flow" }
        }
    }

    suspend fun disconnect() {
        webSocketSession.close(
            CloseReason(CloseReason.Codes.NORMAL, "Disconnect")
        )
    }

    private suspend fun DefaultClientWebSocketSession.handleMessage(frame: Frame): MessageResponseDto? {
        return when (frame) {
            is Frame.Text -> converter?.deserialize(frame)
            is Frame.Close -> {
                disconnect()
                null
            }
            else -> null
        }
    }
}