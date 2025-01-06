package org.example.project.kmmchat.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.example.project.kmmchat.domain.repository.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetTokenUseCase: KoinComponent {
    private val userRepository: UserRepository by inject()
    operator fun invoke(): Flow<String?> {
        return userRepository.getToken()
    }
}