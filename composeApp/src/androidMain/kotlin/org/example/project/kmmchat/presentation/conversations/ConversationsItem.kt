package org.example.project.kmmchat.presentation.conversations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.project.kmmchat.util.ChatType

@Composable
fun ConversationsItem(
    modifier: Modifier = Modifier,
    conversation: ConversationUI,
    onConversationClick: (conversationId: String, conversationType: ChatType, name: String) -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .height(50.dp)
            .clickable {
                onConversationClick(
                    conversation.conversationId,
                    conversation.conversationType,
                    conversation.name
                )
            },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
//        Avatar(
//            modifier = Modifier.size(50.dp),
//            url = "https://i.pravatar.cc/150?u=1",
//            contentDescription = conversation.name
//        )
        Icon(
            modifier = Modifier.size(50.dp),
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "user profile"
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = conversation.name, fontWeight = FontWeight.SemiBold)
            Text(
                text = conversation.messageResponse?.content ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.SpaceEvenly
        ) {
            conversation.messageResponse?.let { Text(text = it.createdAt) }
//            if (conversation.unreadCount > 0) {
//                Box(
//                    modifier = Modifier
//                        .clip(CircleShape)
//                        .size(16.dp)
//                        .background(color = MaterialTheme.colorScheme.primary)
//                ) {
//                    Text(
//                        modifier = Modifier.align(Alignment.Center),
//                        text = conversation.unreadCount.toString(),
//                        color = MaterialTheme.colorScheme.onPrimary,
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                }
//            } else {
//                Spacer(modifier = Modifier.size(16.dp))
//            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewConversationsItem() {
    ConversationsItem(
        conversation = ConversationUI(
            conversationId = "1",
            conversationType = ChatType.CHAT,
            name = "John Doe",
            messageResponse = null
        ), onConversationClick = { n, t, s -> })
}