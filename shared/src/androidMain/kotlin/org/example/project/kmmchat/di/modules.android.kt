package org.example.project.kmmchat.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.project.kmmchat.platform.createDataStore
import org.example.project.kmmchat.presentation.MainViewModel
import org.example.project.kmmchat.presentation.add_room_members.AddRoomMemberViewModel
import org.example.project.kmmchat.presentation.auth.ForgotPasswordViewModel
import org.example.project.kmmchat.presentation.auth.OtpAccountVerifyViewModel
import org.example.project.kmmchat.presentation.auth.OtpPassVerifyViewModel
import org.example.project.kmmchat.presentation.auth.ResetPasswordViewModel
import org.example.project.kmmchat.presentation.auth.SignInViewModel
import org.example.project.kmmchat.presentation.auth.SignUpViewModel
import org.example.project.kmmchat.presentation.chat.ChatViewModel
import org.example.project.kmmchat.presentation.conversations.ConversationsViewModel
import org.example.project.kmmchat.presentation.new_conversation.NewConversationViewModel
import org.example.project.kmmchat.presentation.new_room.NewRoomViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val utilityModule = module {
    single(qualifier = named(Qualifier.WEBSOCKET_URL)) { "ws://10.0.2.2:3001" }
    single(qualifier = named(Qualifier.API_URL)) { "http://10.0.2.2:8080/api/v1" }
}

actual val platformModule = module {
    single(qualifier = named(Qualifier.WEBSOCKET_CLIENT)) {
        HttpClient(OkHttp) {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
        }
    }
    single(qualifier = named(Qualifier.API_CLIENT)) {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    // repository
    single {
        createDataStore(androidApplication())
    }

    // viewModel
    viewModelOf(::SignUpViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::OtpPassVerifyViewModel)
    viewModelOf(::OtpAccountVerifyViewModel)
    viewModelOf(::ResetPasswordViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::OtpPassVerifyViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::ConversationsViewModel)
    viewModelOf(::ChatViewModel)
    viewModelOf(::NewConversationViewModel)
    viewModelOf(::NewRoomViewModel)
    viewModelOf(::AddRoomMemberViewModel)
}