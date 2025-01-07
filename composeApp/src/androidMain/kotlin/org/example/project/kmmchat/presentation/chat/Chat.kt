package org.example.project.kmmchat.presentation.chat

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.kmmchat.util.ChatType
import org.example.project.kmmchat.presentation.common.CircularIndicatorBox
import org.example.project.kmmchat.presentation.conversations.MessageResponseUI
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chat(
    modifier: Modifier = Modifier,
    conversationId: String,
    conversationType: ChatType,
    name: String,
    onNavigateBack: () -> Unit,
    viewModel: ChatViewModel = koinViewModel()
) {
    val chatUiState by viewModel.chat.collectAsStateWithLifecycle()
    val isLoading by viewModel.loading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val text by viewModel.text.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    LaunchedEffect(Unit) {
        viewModel.initializeChat(conversationId, conversationType, name)
    }

    LaunchedEffect(chatUiState) {
        if(chatUiState.messages.isNotEmpty()){
            listState.scrollToItem(index = chatUiState.messages.size - 1)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = chatUiState.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }, navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "back button"
                    )
                }
            })
        },
        bottomBar = {
            SendMessage(
                text = text,
                onValueChange = { viewModel.onTextChange(it) },
                onClickSend = {
                    viewModel.sendMessage()
                })
        }
    ) { paddingValues ->
        CircularIndicatorBox(modifier = Modifier.padding(paddingValues), isLoading = isLoading) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = listState
            ) {
                items(
                    items = chatUiState.messages,
                    key = { item: MessageResponseUI -> item.id }) { message ->
                    MessageItem(message = message)
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewChat() {
    Chat(conversationId = "Alice", conversationType = ChatType.CHAT, name = "", onNavigateBack = {})
}