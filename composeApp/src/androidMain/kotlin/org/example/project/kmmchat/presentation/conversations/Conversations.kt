package org.example.project.kmmchat.presentation.conversations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
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
import org.example.project.kmmchat.presentation.common.ErrorScreen
import org.example.project.kmmchat.util.ChatType
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Conversations(
    modifier: Modifier = Modifier,
    viewModel: ConversationsViewModel = koinViewModel(),
    onNewConversationClick: () -> Unit,
    onClickNewRoom: () -> Unit,
    onConversationClick: (conversationId: String, conversationType: ChatType, name: String) -> Unit
) {
    val conversationsUi by viewModel.conversationUiState.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.shadow(4.dp),
                title = {
                    Text(
                        "Conversations",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
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
                                    Text(text = "New Group", textAlign = TextAlign.Center)
                                },
                                onClick = {
                                    expanded = false
                                    onClickNewRoom()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = "Logout", textAlign = TextAlign.Center)
                                },
                                onClick = {
                                    expanded = false
                                    viewModel.logout()
                                }
                            )
                        }
                    }
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
            if (error != null) {
                ErrorScreen(text = error ?: "Something Went Wrong")
            } else {
                LazyColumn(
                    modifier = Modifier.padding(8.dp).fillMaxSize()
                ) {
                    items(
                        conversationsUi.conversations,
                        key = { item: ConversationUI -> item.conversationId }) { conversation ->
                        ConversationsItem(
                            conversation = conversation,
                            onConversationClick = onConversationClick
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewConversations() {
    Conversations(onNewConversationClick = {}, onClickNewRoom = {}, onConversationClick = { x, y, z -> })
}