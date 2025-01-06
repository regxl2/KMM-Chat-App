package org.example.project.kmmchat.domain.repository

import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    companion object{
        val TOKEN = stringPreferencesKey("token")
        val USER_ID = stringPreferencesKey("user_d")
    }
    suspend fun setToken(token: String?)
    suspend fun setUserId(userId: String?)
    fun getToken(): Flow<String?>
    fun getUserId(): Flow<String?>
}