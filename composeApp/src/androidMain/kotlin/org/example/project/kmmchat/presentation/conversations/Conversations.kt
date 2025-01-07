package org.example.project.kmmchat.presentation.conversations

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.kmmchat.util.ChatType
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Conversations(
    modifier: Modifier = Modifier,
    viewModel: ConversationsViewModel = koinViewModel(),
    onNewConversationClick: () -> Unit,
    onConversationClick: (conversationId: String, conversationType: ChatType, name: String) -> Unit
) {
    val conversationsUi by viewModel.conversationUiState.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val userId by viewModel.userId.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "KMM Chat",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewConversationClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "new chat button"
                )
            }
        }
    ) { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier.padding(paddingValues),
            isRefreshing = isLoading,
            onRefresh = {
                viewModel.getConversationList()
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    conversationsUi.conversations,
                    key = { item: ConversationUI -> item.conversationId }) { conversation ->
                    ConversationsItem(
                        userId = userId,
                        conversation = conversation,
                        onConversationClick = onConversationClick
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewConversations() {
    Conversations(onNewConversationClick = {}, onConversationClick = {x, y, z -> })
}