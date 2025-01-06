package org.example.project.kmmchat.domain.usecase

import kotlinx.coroutines.withContext
import org.example.project.kmmchat.domain.model.ResendOtpDetails
import org.example.project.kmmchat.domain.model.Response
import org.example.project.kmmchat.util.Result
import org.example.project.kmmchat.domain.repository.AuthRepository
import org.example.project.kmmchat.platform.DispatcherProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ResendOtpUseCase: KoinComponent {
    private val authRepository: AuthRepository by inject()
    suspend operator fun invoke (resendOtpDetails: ResendOtpDetails): Result<Response> {
        return withContext(DispatcherProvider.io){
            authRepository.resendOtp(resendOtpDetails)
        }
    }
}