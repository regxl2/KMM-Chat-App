package org.example.project.kmmchat.presentation.new_conversation

import dev.icerock.moko.mvvm.flow.cStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.example.project.kmmchat.domain.model.SearchUsersDetails
import org.example.project.kmmchat.domain.repository.CredentialsRepository
import org.example.project.kmmchat.domain.repository.UserRepository
import org.example.project.kmmchat.presentation.common.SearchUiState
import org.example.project.kmmchat.util.Result

class NewConversationViewModel(
    private val userRepository: UserRepository,
    private val credentialsRepository: CredentialsRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow().cStateFlow()

    private var job: Job? = null

    private val _searchUiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchUiState = _searchUiState.asStateFlow().cStateFlow()

    fun onQueryChange(newQuery: String) {
        if(_query.value == newQuery ) return
        _query.value = newQuery
        if (newQuery.isEmpty()){
            _searchUiState.value = SearchUiState.Idle
            return
        }
        job?.cancel()
        job = viewModelScope.launch {
            delay(1000)
            _searchUiState.value = SearchUiState.Loading
            val token = credentialsRepository.getToken().firstOrNull()
            if(token == null){
                _searchUiState.value = SearchUiState.Error(message = "Invalid Token")
            }
            else{
                val searchUsersDetails = SearchUsersDetails(query = newQuery, token = token)
                val result = userRepository.searchUsers(searchUsersDetails = searchUsersDetails)
                _searchUiState.value = when (result) {
                    is Result.Success -> {
                        if (result.data != null) {
                            SearchUiState.Result(users = result.data.users)
                        } else {
                            SearchUiState.Error(message = "No User Found")
                        }
                    }

                    is Result.Error -> SearchUiState.Error(
                        message = result.message ?: "Something Went Wrong"
                    )
                }
            }
        }
    }

    fun clearStates(){
        _query.value = ""
        _searchUiState.value = SearchUiState.Idle
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }
}