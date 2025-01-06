package org.example.project.kmmchat.presentation.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SendMessage(modifier: Modifier = Modifier, text: String, onValueChange: (String) -> Unit,  onClickSend: ()-> Unit) {
    Box(
        modifier = modifier
            .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .align(Alignment.CenterStart)
                .height(56.dp),
            value = text,
            onValueChange = onValueChange )
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .height(56.dp),
            enabled = text.isNotEmpty(),
            onClick = onClickSend) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.Send, contentDescription = "send message",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSendMessage() {
    SendMessage(text = "", onValueChange = {}, onClickSend = {})
}