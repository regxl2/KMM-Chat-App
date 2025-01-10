package org.example.project.kmmchat.domain.usecase

import org.example.project.kmmchat.domain.repository.CredentialsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SetUserIdUseCase: KoinComponent {
    private val credentialsRepository: CredentialsRepository by inject()
    suspend operator fun invoke(userId: String?){
        credentialsRepository.setUserId(userId)
    }
}