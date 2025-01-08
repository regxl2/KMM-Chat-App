package org.example.project.kmmchat.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ErrorScreen(modifier: Modifier = Modifier, text: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            textAlign = TextAlign.Center,
            color = Color.Red
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewErrorScreen() {
    ErrorScreen(text = "Something Went Wrong")
}