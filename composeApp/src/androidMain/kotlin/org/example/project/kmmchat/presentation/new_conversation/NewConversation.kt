package org.example.project.kmmchat.presentation.new_conversation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NewConversation(modifier: Modifier = Modifier, onNavigateBack: ()-> Unit) {
    Text("New Conversation")
}

@Preview
@Composable
private fun PreviewNewConversation() {
    NewConversation(onNavigateBack = {})
}