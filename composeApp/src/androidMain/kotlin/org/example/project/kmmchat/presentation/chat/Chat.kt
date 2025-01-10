package org.example.project.kmmchat.presentation.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.kmmchat.presentation.common.CircularIndicatorBox
import org.example.project.kmmchat.presentation.common.ErrorScreen
import org.example.project.kmmchat.presentation.common.MessageResponseUI
import org.example.project.kmmchat.util.ChatType
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chat(
    modifier: Modifier = Modifier,
    conversationId: String,
    conversationType: ChatType,
    name: String,
    onNavigateBack: () -> Unit,
    onClickAddGroupMember: () -> Unit,
    viewModel: ChatViewModel = koinViewModel()
) {
    val chatUiState by viewModel.chat.collectAsStateWithLifecycle()
    val isLoading by viewModel.loading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val text by viewModel.text.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    var expanded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.initializeChat(conversationId, conversationType, name)
    }

    LaunchedEffect(chatUiState) {
        if (chatUiState.messages.isNotEmpty()) {
            listState.scrollToItem(index = chatUiState.messages.size - 1)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.shadow(4.dp),
                title = {
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
                },
                actions = {
                    if(conversationType == ChatType.ROOM){
                        Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(text = "Add members", textAlign = TextAlign.Center)
                                    },
                                    onClick = {
                                        expanded = false
                                        onClickAddGroupMember()
                                    }
                                )
                            }
                        }
                    }
                }
            )
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
            if (error != null) {
                ErrorScreen(text = error ?: "Something Went Wrong")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(8.dp)
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
}

@Preview
@Composable
private fun PreviewChat() {
    Chat(conversationId = "Alice", conversationType = ChatType.CHAT, name = "", onNavigateBack = {}, onClickAddGroupMember = {})
}