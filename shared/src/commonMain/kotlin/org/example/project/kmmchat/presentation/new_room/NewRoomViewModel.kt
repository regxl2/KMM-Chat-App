package org.example.project.kmmchat.presentation.new_room


import dev.icerock.moko.mvvm.flow.cStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.example.project.kmmchat.domain.model.CreateRoom
import org.example.project.kmmchat.domain.repository.ConversationRepository
import org.example.project.kmmchat.domain.repository.CredentialsRepository
import org.example.project.kmmchat.util.Result

class NewRoomViewModel(
    private val conversationRepository: ConversationRepository,
    private val credentialsRepository: CredentialsRepository
) : ViewModel() {
    private val _roomName = MutableStateFlow("")
    val roomName = _roomName.asStateFlow().cStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow().cStateFlow()

    private val _navigate = MutableStateFlow(false)
    val navigate = _navigate.asStateFlow().cStateFlow()

    fun onValueChange(newText: String) {
        _roomName.value = newText
    }

    fun resetNavigate(){
        _navigate.value = false
    }

    fun addRoom() {
        if (roomName.value.isEmpty()){
            _error.value = "Please enter the room name"
            return
        }
        viewModelScope.launch {
            val token = credentialsRepository.getToken().firstOrNull()
            if(token==null){
                _error.value = "Invalid token"
                return@launch
            }
            when(val result = conversationRepository.createRoom(roomDetails = CreateRoom(token = token, roomName = roomName.value))){
                is Result.Success -> {
                    _navigate.value = true
                }
                is Result.Error -> {
                    _error.value = result.message
                }
            }
        }
    }

    fun resetStates(){
        _navigate.value = false
        _error.value = null
        _roomName.value = ""
    }
}