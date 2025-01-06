package org.example.project.kmmchat.domain.repository

import org.example.project.kmmchat.domain.model.ChangePasswordDetails
import org.example.project.kmmchat.domain.model.OtpDetails
import org.example.project.kmmchat.domain.model.ResendOtpDetails
import org.example.project.kmmchat.domain.model.Response
import org.example.project.kmmchat.util.Result
import org.example.project.kmmchat.domain.model.SignInBody
import org.example.project.kmmchat.domain.model.SignUpBody
import org.example.project.kmmchat.domain.model.Token

interface AuthRepository {
    suspend fun authenticate(token: String): Result<Response>
    suspend fun signUp(signUpDetails: SignUpBody): Result<Response>
    suspend fun accountVerification(otpDetails: OtpDetails): Result<Response>
    suspend fun resendOtp(resendOtpDetails: ResendOtpDetails): Result<Response>
    suspend fun signIn(signInBody: SignInBody): Result<Token>
    suspend fun forgotPasswordRequest(email: String): Result<Response>
    suspend fun passResetVerification(otpDetails: OtpDetails): Result<Response>
    suspend fun changePassword(changePasswordDetails: ChangePasswordDetails): Result<Response>
}