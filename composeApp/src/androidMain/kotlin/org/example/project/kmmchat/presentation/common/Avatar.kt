package org.example.project.kmmchat.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    url: String,
    contentDescription: String? = "User Avatar"
) {
    AsyncImage(
        modifier = modifier.clip(CircleShape).size(50.dp),
        model = url,
        contentDescription = contentDescription
    )
}