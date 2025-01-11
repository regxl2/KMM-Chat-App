package org.example.project.kmmchat.util

import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.utils.io.errors.IOException

suspend fun <T> runSafely(block: suspend ()-> Result<T>): Result<T>{
    return try{
        block()
    }
    catch (e: IOException) {
        e.printStackTrace()
        Result.Error(message = "Network error: Please check your internet connection and try again.")
    } catch (e: HttpRequestTimeoutException) {
        e.printStackTrace()
        Result.Error(message = "Request timed out: The server took too long to respond.")
    } catch (e: ResponseException) {
        e.printStackTrace()
        Result.Error(message = "Server error: ${e.response.status.value} - ${e.message}")
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error( message = "An unexpected error occurred: ${e.message ?: "Unknown error"}")
    }
}