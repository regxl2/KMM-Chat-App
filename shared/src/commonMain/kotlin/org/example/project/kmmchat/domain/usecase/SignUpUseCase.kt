package org.example.project.kmmchat.domain.usecase

import org.example.project.kmmchat.domain.model.Response
import org.example.project.kmmchat.domain.model.SignUpBody
import org.example.project.kmmchat.domain.repository.AuthRepository
import org.example.project.kmmchat.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SignUpUseCase: KoinComponent {
    private val authRepository: AuthRepository by inject()
    suspend operator fun invoke(signUpDetails: SignUpBody): Result<Response> {
        return authRepository.signUp(signUpDetails)
    }
}