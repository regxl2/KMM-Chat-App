package org.example.project.kmmchat.data.repository

import kotlinx.coroutines.withContext
import org.example.project.kmmchat.data.remote.auth_data_source.RemoteAuthDataSource
import org.example.project.kmmchat.data.remote.common_dto.ResponseDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.toAccountVerificationDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.toChangePasswordDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.toPassResetVerificationDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.toResendOtpDto
import org.example.project.kmmchat.data.remote.common_dto.toResponse
import org.example.project.kmmchat.data.remote.auth_data_source.dto.toSignInBodyDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.toSignUpDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.toToken
import org.example.project.kmmchat.domain.model.ChangePasswordDetails
import org.example.project.kmmchat.domain.model.OtpDetails
import org.example.project.kmmchat.domain.model.ResendOtpDetails
import org.example.project.kmmchat.domain.model.ResendOtpType
import org.example.project.kmmchat.domain.model.Response
import org.example.project.kmmchat.domain.model.SignInBody
import org.example.project.kmmchat.domain.model.SignUpBody
import org.example.project.kmmchat.domain.model.Token
import org.example.project.kmmchat.domain.repository.AuthRepository
import org.example.project.kmmchat.platform.DispatcherProvider
import org.example.project.kmmchat.util.Result

class AuthRepositoryImpl(private val remoteAuthDataSource: RemoteAuthDataSource) : AuthRepository {

    override suspend fun authenticate(token: String): Result<Response> {
        return withContext(DispatcherProvider.io){
            val responseDto = remoteAuthDataSource.authenticate(token = token)
            toResultResponse(responseDto)
        }
    }

    override suspend fun signUp(signUpDetails: SignUpBody): Result<Response> {
        return withContext(DispatcherProvider.io) {
            val responseDto = remoteAuthDataSource.signUp(details = signUpDetails.toSignUpDto())
            toResultResponse(responseDto)
        }
    }

    override suspend fun accountVerification(otpDetails: OtpDetails): Result<Response> {
        return withContext(DispatcherProvider.io) {
            val responseDto = remoteAuthDataSource.accountVerification(accountVerificationDetails = otpDetails.toAccountVerificationDto())
            toResultResponse(responseDto)
        }
    }

    override suspend fun resendOtp(resendOtpDetails: ResendOtpDetails): Result<Response> {
        return withContext(DispatcherProvider.io) {
            val pathSegment = if(resendOtpDetails.type == ResendOtpType.ACCOUNT_VERIFICATION) "resend-otp-signup" else "resend-otp-pass"
            val responseDto = remoteAuthDataSource.resendOtp(pathSegment = pathSegment, resendOtpDto = resendOtpDetails.toResendOtpDto())
            toResultResponse(responseDto)
        }
    }

    override suspend fun signIn(signInBody: SignInBody): Result<Token> {
        return withContext(DispatcherProvider.io) {
            when(val tokenDto = remoteAuthDataSource.signIn(signInBodyDto = signInBody.toSignInBodyDto())){
                is Result.Success -> Result.Success(data = tokenDto.data?.toToken())
                is Result.Error -> Result.Error(message = tokenDto.message)
            }

        }
    }

    override suspend fun forgotPasswordRequest(email: String): Result<Response> {
        return withContext(DispatcherProvider.io) {
            val responseDto = remoteAuthDataSource.forgotPasswordRequest(email = email)
            toResultResponse(responseDto)
        }
    }

    override suspend fun passResetVerification(otpDetails: OtpDetails): Result<Response> {
        return withContext(DispatcherProvider.io) {
            val responseDto = remoteAuthDataSource.passResetVerification(passResetDto = otpDetails.toPassResetVerificationDto())
            toResultResponse(responseDto)
        }
    }

    override suspend fun changePassword(changePasswordDetails: ChangePasswordDetails): Result<Response> {
        return withContext(DispatcherProvider.io){
            val responseDto = remoteAuthDataSource.changePassword(changePasswordDto = changePasswordDetails.toChangePasswordDto())
            toResultResponse(responseDto)
        }
    }

    private fun toResultResponse(responseDto: Result<ResponseDto>): Result<Response>{
        return when(responseDto){
            is Result.Success -> Result.Success(data = responseDto.data?.toResponse())
            is Result.Error -> Result.Error(message = responseDto.message)
        }
    }
}