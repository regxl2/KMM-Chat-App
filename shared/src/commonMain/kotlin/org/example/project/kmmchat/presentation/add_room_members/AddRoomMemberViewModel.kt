package org.example.project.kmmchat.presentation.add_room_members

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.example.project.kmmchat.domain.model.AddRoomUserDetails
import org.example.project.kmmchat.domain.model.SearchUsersDetails
import org.example.project.kmmchat.domain.repository.ChatRepository
import org.example.project.kmmchat.domain.repository.CredentialsRepository
import org.example.project.kmmchat.domain.repository.UserRepository
import org.example.project.kmmchat.presentation.common.SearchUiState
import org.example.project.kmmchat.util.Result

class AddRoomMemberViewModel(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val credentialsRepository: CredentialsRepository
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _searchUiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchUiState = _searchUiState.asStateFlow()

    private var conversationId: String = ""

    private var job: Job? = null

    fun setConversationId(id: String) {
        conversationId = id
    }

    fun onQueryChange(newQuery: String) {
        if (_query.value == newQuery) return
        _query.value = newQuery
        if (newQuery.isEmpty()) return
        job?.cancel()
        job = viewModelScope.launch {
            delay(1000)
            _searchUiState.value = SearchUiState.Loading
            val token = credentialsRepository.getToken().firstOrNull()
            if (token == null) {
                _searchUiState.value = SearchUiState.Error("Invalid token")
                return@launch
            }
            val searchUsersDetails = SearchUsersDetails(conversationId, _query.value, token)
            when (val result = userRepository.searchForAddingRoomUsers(searchUsersDetails)) {
                is Result.Success -> {
                    if (result.data == null) {
                        _searchUiState.value = SearchUiState.Error("No user found")
                    } else {
                        _searchUiState.value = SearchUiState.Result(result.data.users)
                    }
                }

                is Result.Error -> {
                    _searchUiState.value =
                        SearchUiState.Error(result.message ?: "Something went wrong")
                }
            }
        }
    }

    fun addUserToRoom(userId: String, index: Int) {
        viewModelScope.launch {
            val token = credentialsRepository.getToken().firstOrNull()
            if (token != null) {
                val addRoomUserDetails =
                    AddRoomUserDetails(conversationId, userId, token)
                when (chatRepository.addUserToRoomChat(addRoomUserDetails)) {
                    is Result.Success -> {
                        if (_searchUiState.value is SearchUiState.Result) {
                            val oldSearchUiStateValue = _searchUiState.value as SearchUiState.Result
                            _searchUiState.value = oldSearchUiStateValue.copy(
                                users = oldSearchUiStateValue.users.mapIndexed { i, user ->
                                    if (i == index) user.copy(isRoomMember = true) else user
                                })
                        }
                    }
                    is Result.Error -> {}
                }
            }
        }
    }

    fun clearStates(){
        job?.cancel()
        job = null
        _query.value = ""
        conversationId = ""
        _searchUiState.value = SearchUiState.Idle
    }
}