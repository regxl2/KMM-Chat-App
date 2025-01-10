package org.example.project.kmmchat.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.example.project.kmmchat.domain.repository.CredentialsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetUserIdUseCase: KoinComponent {
    private val credentialsRepository: CredentialsRepository by inject()
    operator fun invoke(): Flow<String?> {
        return credentialsRepository.getUserId()
    }
}