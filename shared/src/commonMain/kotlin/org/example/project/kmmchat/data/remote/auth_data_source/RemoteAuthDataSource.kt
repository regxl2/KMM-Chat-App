package org.example.project.kmmchat.data.remote.auth_data_source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import org.example.project.kmmchat.data.remote.auth_data_source.dto.ForgotPasswordDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.ResendOtpDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.ResponseDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.TokenDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.toAccountVerificationDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.toChangePasswordDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.toPassResetVerificationDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.toResponse
import org.example.project.kmmchat.data.remote.auth_data_source.dto.toSignInBodyDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.toSignUpDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.toToken
import org.example.project.kmmchat.data.remote.common_dto.ErrorDto
import org.example.project.kmmchat.domain.model.ChangePasswordDetails
import org.example.project.kmmchat.domain.model.OtpDetails
import org.example.project.kmmchat.domain.model.ResendOtpDetails
import org.example.project.kmmchat.domain.model.ResendOtpType
import org.example.project.kmmchat.domain.model.Response
import org.example.project.kmmchat.util.Result
import org.example.project.kmmchat.domain.model.SignInBody
import org.example.project.kmmchat.domain.model.SignUpBody
import org.example.project.kmmchat.domain.model.Token

class RemoteAuthDataSource(
    private val httpClient: HttpClient,
    apiUrl: String
) {
    private val authUrl: String = "$apiUrl/auth";

    suspend fun authenticate(token: String): Result<Response> {
        val httpResponse =  httpClient.get(urlString = authUrl){
            url {
                appendPathSegments(listOf("authenticate"))
            }
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return getResult(httpResponse = httpResponse)
    }

    suspend fun signUp(details: SignUpBody): Result<Response> {
        val signUpDetails = details.toSignUpDto();
        return request(
            httpClient = httpClient,
            authUrl = authUrl,
            pathSegments = listOf("signup"),
            body = signUpDetails
        )
    }

    suspend fun accountVerification(otpDetails: OtpDetails): Result<Response> {
        val accountVerificationDetails = otpDetails.toAccountVerificationDto()
        return request(
            httpClient = httpClient,
            authUrl = authUrl,
            pathSegments = listOf("verify-signup-otp"),
            body = accountVerificationDetails
        )
    }

    suspend fun resendOtp(resendOtpDetails: ResendOtpDetails): Result<Response> {
        val (email, type) = resendOtpDetails
        val otpDetails = ResendOtpDto(email = email)
        val pathSegment = if (type == ResendOtpType.ACCOUNT_VERIFICATION) "resend-otp-signup"
        else "resend-otp-pass"
        return request(
            httpClient = httpClient,
            authUrl = authUrl,
            pathSegments = listOf(pathSegment),
            body = otpDetails
        )
    }

    suspend fun signIn(signInBody: SignInBody): Result<Token> {
        val signInDetails = signInBody.toSignInBodyDto()
        val httpResponse = httpClient.post(urlString = authUrl) {
            url {
                appendPathSegments("login")
            }
            contentType(ContentType.Application.Json)
            setBody(signInDetails)
        }
        return if (httpResponse.status.isSuccess()) {
            val success = httpResponse.body<TokenDto>().toToken()
            Result.Success(data = success)
        } else {
            val errorMessage = httpResponse.body<ErrorDto>().error
            Result.Error(message = errorMessage)
        }
    }

    suspend fun forgotPasswordRequest(email: String): Result<Response> {
        val forgotPasswordDetails = ForgotPasswordDto(email)
        return request(
            httpClient = httpClient,
            authUrl = authUrl,
            pathSegments = listOf("forgot-password"),
            body = forgotPasswordDetails
        )
    }

    suspend fun passResetVerification(otpDetails: OtpDetails): Result<Response> {
        val passResetDetails = otpDetails.toPassResetVerificationDto()
        return request(
            httpClient = httpClient,
            authUrl = authUrl,
            pathSegments = listOf("verify-reset-otp"),
            body = passResetDetails
        )
    }

    suspend fun changePassword(changePasswordDetails: ChangePasswordDetails): Result<Response> {
        val changePasswordDto = changePasswordDetails.toChangePasswordDto()
        return request(
            httpClient = httpClient,
            authUrl = authUrl,
            pathSegments = listOf("change-password"),
            body = changePasswordDto
        )
    }

    private suspend fun getResult(httpResponse: HttpResponse): Result<Response> {
        return if (httpResponse.status.isSuccess()) {
            val success = httpResponse.body<ResponseDto>().toResponse()
            Result.Success(data = success)
        } else {
            val errorMessage = httpResponse.body<ErrorDto>().error
            println(errorMessage)
            Result.Error(message = errorMessage)
        }
    }

    private suspend inline fun <reified T> request(
        httpClient: HttpClient,
        authUrl: String,
        pathSegments: List<String>,
        body: T
    ): Result<Response> {
        val httpResponse = httpClient.post(urlString = authUrl) {
            url {
                appendPathSegments(pathSegments)
            }
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return getResult(httpResponse = httpResponse)
    }
}