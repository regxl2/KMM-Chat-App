package org.example.project.kmmchat.domain.usecase

import org.example.project.kmmchat.domain.model.Response
import org.example.project.kmmchat.util.Result
import org.example.project.kmmchat.domain.repository.AuthRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthenticateUseCase : KoinComponent {
    private val authRepository: AuthRepository by inject()
    suspend operator fun invoke(token: String): Result<Response> {
        return authRepository.authenticate(token = token)
    }
}