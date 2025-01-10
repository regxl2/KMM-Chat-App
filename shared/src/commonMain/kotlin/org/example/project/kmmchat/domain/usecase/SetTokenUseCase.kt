package org.example.project.kmmchat.domain.usecase

import org.example.project.kmmchat.domain.repository.CredentialsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SetTokenUseCase : KoinComponent {
    private val credentialsRepository: CredentialsRepository by inject()
    suspend operator fun invoke(token: String) {
        credentialsRepository.setToken(token)
    }
}