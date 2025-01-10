package org.example.project.kmmchat.presentation.add_room_members

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
fun AddMemberBox(
    modifier: Modifier = Modifier,
    user: User,
    onClick: (userId: String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Icons.Default.AccountCircle,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "profile image"
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(text = user.name, fontWeight = FontWeight.SemiBold)
                Text(
                    text = user.email,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Button(
                modifier = Modifier.height(36.dp),
                contentPadding = ButtonDefaults.TextButtonContentPadding,
                onClick = { onClick(user.email) },
                enabled = !user.isRoomMember,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Add")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAddMemberBox() {
    AddMemberBox(user = User(email = "example@gmail.com", name = "example"), onClick = { _ -> })
}