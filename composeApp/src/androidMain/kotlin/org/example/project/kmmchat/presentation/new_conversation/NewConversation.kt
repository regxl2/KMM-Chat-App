package org.example.project.kmmchat.presentation.new_conversation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.kmmchat.domain.model.User
import org.example.project.kmmchat.presentation.common.ErrorScreen
import org.example.project.kmmchat.presentation.common.LoadingScreen
import org.example.project.kmmchat.presentation.common.SearchBox
import org.koin.androidx.compose.koinViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewConversation(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onNavigateChat: (user: User) -> Unit,
    viewModel: NewConversationViewModel = koinViewModel()
) {
    val text by viewModel.query.collectAsStateWithLifecycle()
    val newConversationsUiState by viewModel.newConversationUiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    Icon(
                        modifier = Modifier.width(48.dp).clickable(onClick = onNavigateBack),
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "back button"
                    )
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

        when (newConversationsUiState) {
            NewConversationUiState.Idle -> Box(modifier = subModifier)
            is NewConversationUiState.Loading -> {
                LoadingScreen(modifier = subModifier)
            }
            is NewConversationUiState.Error -> {
                val errorMessage = (newConversationsUiState as NewConversationUiState.Error).message
                ErrorScreen(modifier = subModifier, text = errorMessage)
            }
            is NewConversationUiState.Result -> {
                val users = (newConversationsUiState as NewConversationUiState.Result).users
                LazyColumn(modifier = subModifier) {
                    items(users, key = { user: User -> user.email }) { user ->
                        UserBox(user = user, onClick = onNavigateChat)
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewNewConversation() {
    NewConversation(onNavigateBack = {}, onNavigateChat = { _ -> })
}