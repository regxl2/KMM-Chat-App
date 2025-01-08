package org.example.project.kmmchat.presentation.new_conversation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import org.example.project.kmmchat.domain.model.User

@Composable
fun UserBox(
    modifier: Modifier = Modifier,
    user: User,
    onClick: (user: User) -> Unit
) {
    Row(
        modifier = modifier
            .clickable(onClick = { onClick(user) })
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "profile image"
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(text = user.name, fontWeight = FontWeight.SemiBold)
            Text(
                text = user.email,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewUser() {
    UserBox(user = User(email = "example@gmail.com", name = "example"), onClick = {_ -> })
}