package org.example.project.kmmchat.di

import org.example.project.kmmchat.data.remote.auth_data_source.RemoteAuthDataSource
import org.example.project.kmmchat.data.remote.chat_data_source.ChatDataSource
import org.example.project.kmmchat.data.remote.conversation_data_source.ConversationDataSource
import org.example.project.kmmchat.data.remote.websocket_data_source.WebSocketDataSource
import org.example.project.kmmchat.data.repository.AuthRepositoryImpl
import org.example.project.kmmchat.data.repository.ChatRepositoryImpl
import org.example.project.kmmchat.data.repository.ConversationRepositoryImpl
import org.example.project.kmmchat.data.repository.UserRepositoryImpl
import org.example.project.kmmchat.domain.repository.AuthRepository
import org.example.project.kmmchat.domain.repository.ChatRepository
import org.example.project.kmmchat.domain.repository.ConversationRepository
import org.example.project.kmmchat.domain.repository.UserRepository
import org.example.project.kmmchat.domain.usecase.AccountVerificationUseCase
import org.example.project.kmmchat.domain.usecase.AuthenticateUseCase
import org.example.project.kmmchat.domain.usecase.ChangePasswordUseCase
import org.example.project.kmmchat.domain.usecase.ForgotPasswordRequestUseCase
import org.example.project.kmmchat.domain.usecase.GetTokenUseCase
import org.example.project.kmmchat.domain.usecase.GetUserIdUseCase
import org.example.project.kmmchat.domain.usecase.PassResetVerificationUseCase
import org.example.project.kmmchat.domain.usecase.ResendOtpUseCase
import org.example.project.kmmchat.domain.usecase.SetTokenUseCase
import org.example.project.kmmchat.domain.usecase.SetUserIdUseCase
import org.example.project.kmmchat.domain.usecase.SignInUseCase
import org.example.project.kmmchat.domain.usecase.SignUpUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
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
}

val repositoryModule = module {
    singleOf(::AuthRepositoryImpl).bind(AuthRepository::class)
    singleOf(::UserRepositoryImpl).bind(UserRepository::class)
    factoryOf(::ChatRepositoryImpl).bind(ChatRepository::class)
    factoryOf(::ConversationRepositoryImpl).bind(ConversationRepository::class)
}

val useCaseModule = module {
    factory { SignUpUseCase() }
    factory { AccountVerificationUseCase() }
    factory { ResendOtpUseCase() }
    factory { SignInUseCase() }
    factory { ForgotPasswordRequestUseCase() }
    factory { PassResetVerificationUseCase() }
    factory { ChangePasswordUseCase() }
    factory { SetTokenUseCase() }
    factory { GetTokenUseCase() }
    factory { AuthenticateUseCase() }
    factory { GetUserIdUseCase() }
    factory { SetUserIdUseCase() }
}

fun getSharedModule() = listOf(dataSourceModule, repositoryModule, useCaseModule, utilityModule)