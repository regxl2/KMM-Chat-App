package org.example.project.kmmchat.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.example.project.kmmchat.util.ContentType
import org.example.project.kmmchat.presentation.common.MessageResponseUI

@Composable
fun MessageItem(modifier: Modifier = Modifier, message: MessageResponseUI) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .align(if (message.isMine) Alignment.CenterEnd else Alignment.CenterStart),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (!message.isMine) {
//                Avatar(
//                    url = "https://i.pravatar.cc/150?u=1",
//                    contentDescription = "${message.senderName}'s avatar"
//                )
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "user profile"
                )
            }
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (!message.isMine) {
                    Text(text = message.senderName, fontWeight = FontWeight.SemiBold)
                }
                when (message.contentType) {
                    ContentType.TEXT->
                        Text(
                            modifier = Modifier
                                .background(
                                    color = if (message.isMine) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.secondary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp),
                            text = message.content,
                            color = if (message.isMine) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
                        )

                    ContentType.IMAGE->
                        AsyncImage(
                            modifier = Modifier.size(50.dp),
                            model = message.content,
                            contentDescription = "image",
                            contentScale = ContentScale.Crop
                        )
                }
                Text(text = message.createdAt, fontSize =  12.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMessageItem() {
    MessageItem(message = MessageResponseUI(
        id = "1",
        content = "Hey, how are you?",
        contentType = ContentType.TEXT,
        isMine = false,
        senderName = "John Dow",
        senderId = "123",
        createdAt = "9:11, AM"
    )
    )
}