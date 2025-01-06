package org.example.project.kmmchat.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.example.project.kmmchat.domain.repository.UserRepository

class UserRepositoryImpl(private val dataStore: DataStore<Preferences>) : UserRepository {
    override suspend fun setToken(token: String?) {
        try{
            dataStore.edit { settings ->
                if(token==null){
                    settings.remove(UserRepository.TOKEN)
                }
                else{
                    settings[UserRepository.TOKEN] = token
                }
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun setUserId(userId: String?) {
        try{
            dataStore.edit { settings ->
                if(userId==null){
                    settings.remove(UserRepository.USER_ID)
                }
                else{
                    settings[UserRepository.USER_ID] = userId
                }
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun getToken(): Flow<String?> {
        return try {
            dataStore.data.map { preferences -> preferences[UserRepository.TOKEN] }
        } catch (e: Exception) {
            e.printStackTrace()
            flowOf(null)
        }
    }

    override fun getUserId(): Flow<String?> {
        return try {
            dataStore.data.map { preferences -> preferences[UserRepository.USER_ID] }
        } catch (e: Exception) {
            e.printStackTrace()
            flowOf(null)
        }
    }
}