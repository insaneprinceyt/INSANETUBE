package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserAccountEntity
import com.example.data.model.Video
import com.example.ui.theme.InsaneRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoReviewSheet(
    currentAccount: UserAccountEntity?,
    allVideos: List<Video>,
    onDismiss: () -> Unit,
    onRemoveVideo: (String) -> Unit,
    onRemoveAllFakeVideos: () -> Unit,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All") } // "All", "Fake", "Real"
    var showConfirmBulkRemove by remember { mutableStateOf(false) }

    val fakeVideos = remember(allVideos) { allVideos.filter { it.isFake } }
    val realVideos = remember(allVideos) { allVideos.filter { !it.isFake } }

    val displayedVideos = remember(allVideos, selectedFilter) {
        when (selectedFilter) {
            "Fake" -> fakeVideos
            "Real" -> realVideos
            else -> allVideos
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.testTag("video_review_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(InsaneRed.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = InsaneRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Video Review & Moderation",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = if (currentAccount != null) "Moderator: ${currentAccount.name}" else "Member verification required",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onBackground)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Non-member guard
            if (currentAccount == null) {
                Surface(
                    color = InsaneRed.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, InsaneRed.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = InsaneRed, modifier = Modifier.size(32.dp))
                        Text(
                            text = "Only Real Members Can Review & Remove Fake Videos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Sign in to your registered channel account to access content moderation, review videos, and remove fake content.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Button(
                            onClick = {
                                onDismiss()
                                onSignInClick()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = InsaneRed),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Sign In to Channel Account", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                return@ModalBottomSheet
            }

            // Overview stats card
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${fakeVideos.size} Fake / Sample Videos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFFF87171)
                        )
                        Text(
                            text = "${realVideos.size} Real Member Uploads",
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = Color(0xFF34D399)
                        )
                    }

                    if (fakeVideos.isNotEmpty()) {
                        Button(
                            onClick = { showConfirmBulkRemove = true },
                            colors = ButtonDefaults.buttonColors(containerColor = InsaneRed),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Remove All Fake", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Filter chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == "All",
                    onClick = { selectedFilter = "All" },
                    label = { Text("All (${allVideos.size})", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedFilter == "Fake",
                    onClick = { selectedFilter = "Fake" },
                    label = { Text("🤖 Fake / Sample (${fakeVideos.size})", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedFilter == "Real",
                    onClick = { selectedFilter = "Real" },
                    label = { Text("✅ Real Uploads (${realVideos.size})", fontSize = 11.sp) }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Videos review list
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (displayedVideos.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No videos in this review category.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    items(displayedVideos, key = { it.id }) { video ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (video.isFake) Color(0xFFF87171).copy(alpha = 0.3f) else Color(0xFF34D399).copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Thumbnail preview
                                Box(
                                    modifier = Modifier
                                        .size(width = 72.dp, height = 44.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            Brush.linearGradient(
                                                listOf(Color(video.gradientStartHex), Color(video.gradientEndHex))
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (video.isShort) Icons.Default.Bolt else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color.White.copy(alpha = 0.8f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                // Details
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = video.title,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = "${video.channelName} • ${video.duration}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    // Badge
                                    Spacer(modifier = Modifier.height(2.dp))
                                    if (video.isFake) {
                                        Surface(
                                            color = Color(0xFFF87171).copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "🤖 FAKE / SAMPLE",
                                                color = Color(0xFFF87171),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    } else {
                                        Surface(
                                            color = Color(0xFF34D399).copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "✅ REAL MEMBER UPLOAD",
                                                color = Color(0xFF34D399),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }

                                // Delete / Remove Action
                                IconButton(
                                    onClick = { onRemoveVideo(video.id) },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color.Red.copy(alpha = 0.1f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove Video",
                                        tint = InsaneRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Confirmation Alert for bulk removal
    if (showConfirmBulkRemove) {
        AlertDialog(
            onDismissRequest = { showConfirmBulkRemove = false },
            title = { Text("Remove All Fake Videos?", fontWeight = FontWeight.Bold) },
            text = {
                Text("This will purge all sample/fake placeholder videos and shorts from INSANETUBE. Only real videos created by members will remain.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmBulkRemove = false
                        onRemoveAllFakeVideos()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = InsaneRed)
                ) {
                    Text("Yes, Remove All Fake", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmBulkRemove = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
