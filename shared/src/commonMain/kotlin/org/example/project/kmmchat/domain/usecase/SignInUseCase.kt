package org.example.project.kmmchat.domain.usecase

import org.example.project.kmmchat.util.Result
import org.example.project.kmmchat.domain.model.SignInBody
import org.example.project.kmmchat.domain.model.Token
import org.example.project.kmmchat.domain.repository.AuthRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SignInUseCase : KoinComponent {
    private val authRepository: AuthRepository by inject()
    suspend operator fun invoke(signInBody: SignInBody): Result<Token> {
        return authRepository.signIn(signInBody)
    }
}