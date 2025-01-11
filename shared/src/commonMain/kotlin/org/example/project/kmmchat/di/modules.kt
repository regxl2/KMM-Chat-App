package org.example.project.kmmchat.di

import org.example.project.kmmchat.data.remote.auth_data_source.RemoteAuthDataSource
import org.example.project.kmmchat.data.remote.chat_data_source.ChatDataSource
import org.example.project.kmmchat.data.remote.conversation_data_source.ConversationDataSource
import org.example.project.kmmchat.data.remote.user_data_source.UserDataSource
import org.example.project.kmmchat.data.remote.websocket_data_source.WebSocketDataSource
import org.example.project.kmmchat.data.repository.AuthRepositoryImpl
import org.example.project.kmmchat.data.repository.ChatRepositoryImpl
import org.example.project.kmmchat.data.repository.ConversationRepositoryImpl
import org.example.project.kmmchat.data.repository.CredentialsRepositoryImpl
import org.example.project.kmmchat.data.repository.UserRepositoryImpl
import org.example.project.kmmchat.domain.repository.AuthRepository
import org.example.project.kmmchat.domain.repository.ChatRepository
import org.example.project.kmmchat.domain.repository.ConversationRepository
import org.example.project.kmmchat.domain.repository.CredentialsRepository
import org.example.project.kmmchat.domain.repository.UserRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

enum class Qualifier {
    WEBSOCKET_URL,
    API_URL,
    WEBSOCKET_CLIENT,
    API_CLIENT
}

expect val platformModule: Module
expect val utilityModule: Module

val dataSourceModule = module {
    single {
        WebSocketDataSource(
            get(named(Qualifier.WEBSOCKET_CLIENT)),
            get(named(Qualifier.WEBSOCKET_URL))
        )
    }
    single {
        ChatDataSource(
            get(named(Qualifier.API_CLIENT)),
            get(named(Qualifier.API_URL))
        )
    }
    single {
        RemoteAuthDataSource(
            get(named(Qualifier.API_CLIENT)),
            get(named(Qualifier.API_URL))
        )
    }
    single{
        ConversationDataSource(
            get(named(Qualifier.API_CLIENT)),
            get(named(Qualifier.API_URL))
        )
    }
    single{
        UserDataSource(
            get(named(Qualifier.API_CLIENT) ),
            get(named(Qualifier.API_URL))
        )
    }
}

val repositoryModule = module {
    singleOf(::AuthRepositoryImpl).bind(AuthRepository::class)
    singleOf(::CredentialsRepositoryImpl).bind(CredentialsRepository::class)
    singleOf(::ChatRepositoryImpl).bind(ChatRepository::class)
    singleOf(::ConversationRepositoryImpl).bind(ConversationRepository::class)
    singleOf(::UserRepositoryImpl).bind(UserRepository::class)
}

fun getSharedModule() = listOf(dataSourceModule, repositoryModule, utilityModule)