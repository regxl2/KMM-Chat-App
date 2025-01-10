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
import org.example.project.kmmchat.data.remote.auth_data_source.dto.AccountVerificationDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.ChangePasswordDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.ForgotPasswordDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.PassResetVerificationDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.ResendOtpDto
import org.example.project.kmmchat.data.remote.common_dto.ResponseDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.SignInBodyDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.SignUpBodyDto
import org.example.project.kmmchat.data.remote.auth_data_source.dto.TokenDto
import org.example.project.kmmchat.data.remote.common_dto.ErrorDto
import org.example.project.kmmchat.util.Result

class RemoteAuthDataSource(
    private val httpClient: HttpClient,
    apiUrl: String
) {
    private val authUrl: String = "$apiUrl/auth";

    suspend fun authenticate(token: String): Result<ResponseDto> {
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

    suspend fun signUp(details: SignUpBodyDto): Result<ResponseDto> {
        return request(
            httpClient = httpClient,
            authUrl = authUrl,
            pathSegments = listOf("signup"),
            body = details
        )
    }

    suspend fun accountVerification(accountVerificationDetails: AccountVerificationDto): Result<ResponseDto> {
        return request(
            httpClient = httpClient,
            authUrl = authUrl,
            pathSegments = listOf("verify-signup-otp"),
            body = accountVerificationDetails
        )
    }

    suspend fun resendOtp(pathSegment: String, resendOtpDto: ResendOtpDto): Result<ResponseDto> {
        return request(
            httpClient = httpClient,
            authUrl = authUrl,
            pathSegments = listOf(pathSegment),
            body = resendOtpDto
        )
    }

    suspend fun signIn(signInBodyDto: SignInBodyDto): Result<TokenDto> {
        val httpResponse = httpClient.post(urlString = authUrl) {
            url {
                appendPathSegments("login")
            }
            contentType(ContentType.Application.Json)
            setBody(signInBodyDto)
        }
        return if (httpResponse.status.isSuccess()) {
            val success = httpResponse.body<TokenDto>()
            Result.Success(data = success)
        } else {
            val errorMessage = httpResponse.body<ErrorDto>().error
            Result.Error(message = errorMessage)
        }
    }

    suspend fun forgotPasswordRequest(email: String): Result<ResponseDto> {
        val forgotPasswordDetails = ForgotPasswordDto(email)
        return request(
            httpClient = httpClient,
            authUrl = authUrl,
            pathSegments = listOf("forgot-password"),
            body = forgotPasswordDetails
        )
    }

    suspend fun passResetVerification(passResetDto: PassResetVerificationDto): Result<ResponseDto> {
        return request(
            httpClient = httpClient,
            authUrl = authUrl,
            pathSegments = listOf("verify-reset-otp"),
            body = passResetDto
        )
    }

    suspend fun changePassword(changePasswordDto: ChangePasswordDto): Result<ResponseDto> {
        return request(
            httpClient = httpClient,
            authUrl = authUrl,
            pathSegments = listOf("change-password"),
            body = changePasswordDto
        )
    }

    private suspend fun getResult(httpResponse: HttpResponse): Result<ResponseDto> {
        return if (httpResponse.status.isSuccess()) {
            val success = httpResponse.body<ResponseDto>()
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
    ): Result<ResponseDto> {
        return try{
            val httpResponse = httpClient.post(urlString = authUrl) {
                url {
                    appendPathSegments(pathSegments)
                }
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            return getResult(httpResponse = httpResponse)
        }
        catch (e: Exception){
            e.printStackTrace()
            Result.Error(message = e.message)
        }
    }
}