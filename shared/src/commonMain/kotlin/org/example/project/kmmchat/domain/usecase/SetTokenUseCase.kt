package org.example.project.kmmchat.domain.usecase

import org.example.project.kmmchat.domain.repository.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SetTokenUseCase : KoinComponent {
    private val userRepository: UserRepository by inject()
    suspend operator fun invoke(token: String) {
        userRepository.setToken(token)
    }
}