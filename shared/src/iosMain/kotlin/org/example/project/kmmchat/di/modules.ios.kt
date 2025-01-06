package org.example.project.kmmchat.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.project.kmmchat.platform.createDataStore
import org.example.project.kmmchat.presentation.MainViewModel
import org.example.project.kmmchat.presentation.auth.ForgotPasswordViewModel
import org.example.project.kmmchat.presentation.auth.OtpAccountVerifyViewModel
import org.example.project.kmmchat.presentation.auth.OtpPassVerifyViewModel
import org.example.project.kmmchat.presentation.auth.ResetPasswordViewModel
import org.example.project.kmmchat.presentation.auth.SignInViewModel
import org.example.project.kmmchat.presentation.auth.SignUpViewModel
import org.example.project.kmmchat.presentation.chat.ChatViewModel
import org.example.project.kmmchat.presentation.conversations.ConversationsViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val utilityModule = module {
    single(qualifier = named(Qualifier.WEBSOCKET_URL)) { "ws://localhost:3001" }
    single(qualifier = named(Qualifier.API_URL)) { "http://localhost:8080/api/v1" }
}

actual val platformModule = module {
    single(qualifier = named(Qualifier.WEBSOCKET_CLIENT)) {
        HttpClient(Darwin) {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
        }
    }
    single(qualifier = named(Qualifier.API_CLIENT)) {
        HttpClient(Darwin) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }
    single{
        createDataStore()
    }
    singleOf(::MainViewModel)
    factoryOf(::SignInViewModel)
    factoryOf(::SignUpViewModel)
    factoryOf(::OtpAccountVerifyViewModel)
    factoryOf(::ForgotPasswordViewModel)
    factoryOf(::OtpPassVerifyViewModel)
    factoryOf(::ResetPasswordViewModel)
    factoryOf(::ConversationsViewModel)
    factoryOf(::ChatViewModel)
}

object ViewModelProvider : KoinComponent {
    val conversationsViewModel: ConversationsViewModel by inject()
    val chatViewModel: ChatViewModel by inject()
    val signInViewModel: SignInViewModel by inject()
    val signUpViewModel: SignUpViewModel by inject()
    val otpAccountVerifyViewModel: OtpAccountVerifyViewModel by inject()
    val forgotPasswordViewModel: ForgotPasswordViewModel by inject()
    val otpPasswordViewModel: OtpPassVerifyViewModel by inject()
    val resetPasswordViewModel: ResetPasswordViewModel by inject()
    val mainViewModel: MainViewModel by inject()
}