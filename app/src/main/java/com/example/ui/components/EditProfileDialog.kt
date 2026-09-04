package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserAccountEntity
import com.example.ui.theme.InsaneRed

@Composable
fun EditProfileDialog(
    isOpen: Boolean,
    currentAccount: UserAccountEntity?,
    onDismiss: () -> Unit,
    onSave: (name: String, handle: String) -> Unit,
    onChangeProfilePictureClick: (() -> Unit)? = null
) {
    if (!isOpen || currentAccount == null) return

    var name by remember(currentAccount) { mutableStateOf(currentAccount.name) }
    var handle by remember(currentAccount) { mutableStateOf(currentAccount.handle) }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Channel Profile",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Avatar with Camera Badge
                Box(
                    modifier = Modifier.padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ProfileAvatar(
                        name = currentAccount.name,
                        avatarColorHex = currentAccount.avatarColorHex,
                        avatarUri = currentAccount.avatarUri,
                        size = 72.dp,
                        hasBlueTick = currentAccount.hasBlueTick,
                        isOwner = currentAccount.isOwner,
                        showEditBadge = true,
                        onEditClick = { onChangeProfilePictureClick?.invoke() },
                        onClick = { onChangeProfilePictureClick?.invoke() }
                    )
                }

                if (onChangeProfilePictureClick != null) {
                    TextButton(
                        onClick = onChangeProfilePictureClick,
                        modifier = Modifier.testTag("edit_profile_change_photo_text_button")
                    ) {
                        Text("Change Profile Picture", color = InsaneRed, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }

                Text(
                    text = "Changes to your name and handle will be visible across INSANETUBE.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        error = null
                    },
                    label = { Text("Channel Name") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_profile_name_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = handle,
                    onValueChange = {
                        handle = if (it.startsWith("@")) it else "@$it"
                        error = null
                    },
                    label = { Text("Channel Handle") },
                    leadingIcon = {
                        Icon(Icons.Default.AlternateEmail, contentDescription = null)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_profile_handle_input")
                )

                error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        error = "Channel name cannot be empty"
                    } else if (handle.isBlank() || handle == "@") {
                        error = "Handle cannot be empty"
                    } else {
                        onSave(name, handle)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = InsaneRed),
                modifier = Modifier.testTag("edit_profile_save_button")
            ) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
