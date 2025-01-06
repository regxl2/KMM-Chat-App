package org.example.project.kmmchat.data.repository

import kotlinx.coroutines.withContext
import org.example.project.kmmchat.data.remote.auth_data_source.RemoteAuthDataSource
import org.example.project.kmmchat.domain.model.ChangePasswordDetails
import org.example.project.kmmchat.domain.model.OtpDetails
import org.example.project.kmmchat.domain.model.ResendOtpDetails
import org.example.project.kmmchat.domain.model.Response
import org.example.project.kmmchat.util.Result
import org.example.project.kmmchat.domain.model.SignInBody
import org.example.project.kmmchat.domain.model.SignUpBody
import org.example.project.kmmchat.domain.model.Token
import org.example.project.kmmchat.domain.repository.AuthRepository
import org.example.project.kmmchat.platform.DispatcherProvider

class AuthRepositoryImpl(private val remoteAuthDataSource: RemoteAuthDataSource) : AuthRepository {

    override suspend fun authenticate(token: String): Result<Response> {
        return withContext(DispatcherProvider.io){
            remoteAuthDataSource.authenticate(token)
        }
    }

    override suspend fun signUp(signUpDetails: SignUpBody): Result<Response> {
        return withContext(DispatcherProvider.io) {
            remoteAuthDataSource.signUp(signUpDetails)
        }
    }

    override suspend fun accountVerification(otpDetails: OtpDetails): Result<Response> {
        return withContext(DispatcherProvider.io) {
            remoteAuthDataSource.accountVerification(otpDetails)
        }
    }

    override suspend fun resendOtp(resendOtpDetails: ResendOtpDetails): Result<Response> {
        return withContext(DispatcherProvider.io) {
            remoteAuthDataSource.resendOtp(resendOtpDetails)
        }
    }

    override suspend fun signIn(signInBody: SignInBody): Result<Token> {
        return withContext(DispatcherProvider.io) {
            remoteAuthDataSource.signIn(signInBody)
        }
    }

    override suspend fun forgotPasswordRequest(email: String): Result<Response> {
        return withContext(DispatcherProvider.io) {
            remoteAuthDataSource.forgotPasswordRequest(email)
        }
    }

    override suspend fun passResetVerification(otpDetails: OtpDetails): Result<Response> {
        return withContext(DispatcherProvider.io) {
            remoteAuthDataSource.passResetVerification(otpDetails)
        }
    }

    override suspend fun changePassword(changePasswordDetails: ChangePasswordDetails): Result<Response> {
        return withContext(DispatcherProvider.io){
            remoteAuthDataSource.changePassword(changePasswordDetails)
        }
    }
}