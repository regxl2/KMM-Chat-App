package org.example.project.kmmchat.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun AvatarAlt(modifier: Modifier = Modifier, text: String) {
    Box(modifier = modifier
        .clip(CircleShape)
        .size(50.dp)
        .background(color = MaterialTheme.colorScheme.secondary)) {
        Text(modifier = Modifier.align(Alignment.Center), text = text.uppercase(Locale.ROOT), color = MaterialTheme.colorScheme.onSecondary)
    }
}

@Preview
@Composable
private fun PreviewAvatarAlt() {
    AvatarAlt(text = "a")
}