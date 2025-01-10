package org.example.project.kmmchat.presentation.new_conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.example.project.kmmchat.domain.model.SearchUsersDetails
import org.example.project.kmmchat.domain.repository.UserRepository
import org.example.project.kmmchat.domain.usecase.GetTokenUseCase
import org.example.project.kmmchat.presentation.common.SearchUiState
import org.example.project.kmmchat.util.Result

class NewConversationViewModel(
    private val userRepository: UserRepository,
    private val tokenUseCase: GetTokenUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchUiState: StateFlow<SearchUiState> = query
        .debounce(1000)
        .filter { it.isNotEmpty() }
        .distinctUntilChanged()
        .flatMapLatest { text ->
            flow {
                emit(SearchUiState.Loading)
                val token = tokenUseCase().firstOrNull()
                if (token == null) {
                    emit(SearchUiState.Error(message = "Invalid Token"))
                } else {
                    val searchUsersDetails = SearchUsersDetails(query = text, token = token)
                    val result = userRepository.searchUsers(searchUsersDetails = searchUsersDetails)
                    val state = when (result) {
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
                    emit(state)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SearchUiState.Idle
        )

}