package org.example.project.kmmchat.domain.usecase

import org.example.project.kmmchat.domain.model.ChangePasswordDetails
import org.example.project.kmmchat.domain.model.Response
import org.example.project.kmmchat.util.Result
import org.example.project.kmmchat.domain.repository.AuthRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChangePasswordUseCase: KoinComponent {
    private val authRepository: AuthRepository by inject()
    suspend operator fun invoke(changePasswordDetails: ChangePasswordDetails): Result<Response> {
        return authRepository.changePassword(changePasswordDetails)
    }
}