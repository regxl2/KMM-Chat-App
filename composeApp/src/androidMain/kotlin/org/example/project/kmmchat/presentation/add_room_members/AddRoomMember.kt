package org.example.project.kmmchat.presentation.add_room_members

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.kmmchat.domain.model.User
import org.example.project.kmmchat.presentation.common.ErrorScreen
import org.example.project.kmmchat.presentation.common.LoadingScreen
import org.example.project.kmmchat.presentation.common.SearchBox
import org.example.project.kmmchat.presentation.common.SearchUiState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGroupMember(modifier: Modifier = Modifier, conversationId: String, onNavigateBack: ()-> Unit, viewModel: AddRoomMemberViewModel = koinViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.setConversationId(id = conversationId)
    }
    val text by viewModel.query.collectAsStateWithLifecycle()
    val searchUiState by viewModel.searchUiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack ) {
                        Icon(
                            modifier = Modifier.width(48.dp),
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back button"
                        )
                    }
                },
                actions = {
                    SearchBox(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        text = text,
                        onValueChange = viewModel::onQueryChange
                    )
                }
            )
        }
    ) { paddingValues ->
        val subModifier = Modifier.padding(paddingValues)
        when (searchUiState) {
            SearchUiState.Idle -> Box(modifier = subModifier)
            is SearchUiState.Loading -> {
                LoadingScreen(modifier = subModifier)
            }
            is SearchUiState.Error -> {
                val errorMessage = (searchUiState as SearchUiState.Error).message
                ErrorScreen(modifier = subModifier, text = errorMessage)
            }
            is SearchUiState.Result -> {
                val users = (searchUiState as SearchUiState.Result).users
                LazyColumn(modifier = subModifier.padding(8.dp)) {
                    itemsIndexed(users, key = { _, user: User -> user.email }){ index, user ->
                        AddMemberBox(user = user, onClick = { userId -> viewModel.addUserToRoom(userId, index) })
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAddGroupMember() {
    AddGroupMember(conversationId = "123", onNavigateBack = {})
}