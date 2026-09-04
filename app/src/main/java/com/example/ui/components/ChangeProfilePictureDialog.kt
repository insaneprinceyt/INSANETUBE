package com.example.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.local.UserAccountEntity
import com.example.ui.theme.InsaneRed

data class AvatarPreset(
    val id: String,
    val title: String,
    val imageUrl: String,
    val tag: String
)

val CREATOR_AVATAR_PRESETS = listOf(
    AvatarPreset(
        id = "pro_gamer",
        title = "Pro Gamer",
        imageUrl = "https://images.unsplash.com/photo-1566492031773-4f4e44671857?w=240",
        tag = "Gaming"
    ),
    AvatarPreset(
        id = "cyber_neon",
        title = "Cyber Neon",
        imageUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=240",
        tag = "Cyber"
    ),
    AvatarPreset(
        id = "streamer_girl",
        title = "Streamer",
        imageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=240",
        tag = "Creator"
    ),
    AvatarPreset(
        id = "tech_hacker",
        title = "Tech Dev",
        imageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=240",
        tag = "Tech"
    ),
    AvatarPreset(
        id = "vibrant_artist",
        title = "Vibrant",
        imageUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=240",
        tag = "Studio"
    ),
    AvatarPreset(
        id = "space_explorer",
        title = "Cosmic",
        imageUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=240",
        tag = "Cosmic"
    ),
    AvatarPreset(
        id = "urban_creator",
        title = "Urban",
        imageUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=240",
        tag = "Vlog"
    ),
    AvatarPreset(
        id = "lofi_synth",
        title = "Lo-Fi Beats",
        imageUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=240",
        tag = "Music"
    )
)

val AVATAR_COLOR_PALETTE = listOf(
    0xFFFF0033 to "Insane Red",
    0xFF0284C7 to "Sky Blue",
    0xFF7C3AED to "Neon Purple",
    0xFF10B981 to "Emerald Green",
    0xFFF59E0B to "Sunset Gold",
    0xFFEC4899 to "Magenta Rose",
    0xFF06B6D4 to "Cyber Cyan",
    0xFF4F46E5 to "Indigo Royal"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeProfilePictureDialog(
    isOpen: Boolean,
    account: UserAccountEntity?,
    onDismiss: () -> Unit,
    onSaveAvatarUri: (String?) -> Unit,
    onSaveAvatarColor: (Long) -> Unit,
    onSavePreset: (presetUrl: String, colorHex: Long) -> Unit
) {
    if (!isOpen || account == null) return

    var selectedSection by remember { mutableIntStateOf(0) } // 0: Presets, 1: Colors & Initials
    var previewUri by remember(account) { mutableStateOf(account.avatarUri) }
    var previewColor by remember(account) { mutableStateOf(account.avatarColorHex) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val uriStr = uri.toString()
            previewUri = uriStr
            onSaveAvatarUri(uriStr)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .padding(vertical = 24.dp)
                .testTag("change_profile_picture_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with Close Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Profile Picture",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Live Circular Preview
                Box(
                    modifier = Modifier
                        .size(104.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ProfileAvatar(
                        name = account.name,
                        avatarColorHex = previewColor,
                        avatarUri = previewUri,
                        size = 96.dp,
                        hasBlueTick = account.hasBlueTick,
                        isOwner = account.isOwner
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = account.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = account.handle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons: Choose from Gallery & Remove Photo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = InsaneRed),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("pick_device_photo_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoCamera,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Choose Photo", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }

                    if (!previewUri.isNullOrBlank()) {
                        OutlinedButton(
                            onClick = {
                                previewUri = null
                                onSaveAvatarUri(null)
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("remove_photo_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Remove", fontSize = 13.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section Tabs (Presets vs Colors)
                TabRow(
                    selectedTabIndex = selectedSection,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = selectedSection == 0,
                        onClick = { selectedSection = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Creator Presets", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    )
                    Tab(
                        selected = selectedSection == 1,
                        onClick = { selectedSection = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Palette, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Colors & Initial", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Section 0: Creator Avatars Grid
                if (selectedSection == 0) {
                    Text(
                        text = "Tap any preset to apply it to your channel:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(CREATOR_AVATAR_PRESETS) { preset ->
                            val isSelected = previewUri == preset.imageUrl
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        previewUri = preset.imageUrl
                                        onSavePreset(preset.imageUrl, previewColor)
                                    }
                                    .padding(2.dp)
                                    .testTag("preset_avatar_${preset.id}")
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) InsaneRed else Color.Gray.copy(alpha = 0.3f),
                                            shape = CircleShape
                                        )
                                ) {
                                    AsyncImage(
                                        model = preset.imageUrl,
                                        contentDescription = preset.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Text(
                                    text = preset.title,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 3.dp)
                                )
                            }
                        }
                    }
                } else {
                    // Section 1: Custom Palette & Initials
                    Text(
                        text = "Pick a signature background color for your avatar initial:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(AVATAR_COLOR_PALETTE) { (colorHex, colorName) ->
                            val isSelected = previewColor == colorHex && previewUri.isNullOrBlank()
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable {
                                        previewColor = colorHex
                                        previewUri = null
                                        onSaveAvatarColor(colorHex)
                                        onSaveAvatarUri(null)
                                    }
                                    .testTag("color_preset_${colorName.lowercase().replace(" ", "_")}")
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(Color(colorHex))
                                        .border(
                                            width = if (isSelected) 3.dp else 0.dp,
                                            color = if (isSelected) Color.White else Color.Transparent,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    } else {
                                        Text(
                                            text = account.name.take(1).uppercase(),
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                    }
                                }
                                Text(
                                    text = colorName,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Done Button
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("done_profile_picture_button")
                ) {
                    Text("Done")
                }
            }
        }
    }
}
