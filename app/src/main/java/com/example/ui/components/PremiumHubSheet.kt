package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserAccountEntity
import com.example.ui.theme.InsaneRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumHubSheet(
    account: UserAccountEntity?,
    onTogglePremium: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPremium = account?.isPremium == true

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
        },
        modifier = modifier.testTag("premium_hub_sheet")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header with Premium Brand Logo
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Play Icon Box
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(InsaneRed, Color(0xFFCC0000))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "INSANETUBE",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = (-0.5).sp,
                                        fontSize = 18.sp,
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Surface(
                                    color = Color(0xFF1E293B),
                                    shape = RoundedCornerShape(4.dp),
                                    border = BorderStroke(1.dp, Color(0xFF94A3B8).copy(alpha = 0.5f))
                                ) {
                                    Text(
                                        text = "PREMIUM",
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Membership Status Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = if (isPremium) {
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF1E1B4B),
                                        Color(0xFF312E81),
                                        Color(0xFF4338CA),
                                        Color(0xFF7C3AED)
                                    )
                                )
                            } else {
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF1F2937),
                                        Color(0xFF374151)
                                    )
                                )
                            },
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isPremium) "ACTIVE MEMBERSHIP" else "SUBSCRIPTION STATUS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.8f),
                                letterSpacing = 1.sp
                            )

                            Surface(
                                color = if (isPremium) Color(0xFF10B981) else Color(0xFF6B7280),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (isPremium) "Active • $13.99/mo" else "Free Plan",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (isPremium) "All Premium Perks Unlocked" else "Upgrade to YouTube Premium",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )

                        Text(
                            text = if (isPremium)
                                "Logged in as ${account?.name ?: "User"} • Ad-free videos, background play, and 4K downloads are currently enabled."
                            else
                                "Watch videos uninterrupted by ads, keep playing when you switch apps, and download your favorites offline.",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                        )

                        Button(
                            onClick = onTogglePremium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isPremium) Color(0xFFEF4444) else InsaneRed,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                                .testTag("toggle_premium_button")
                        ) {
                            Icon(
                                imageVector = if (isPremium) Icons.Default.Cancel else Icons.Default.Stars,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isPremium) "Cancel Premium Subscription" else "Activate YouTube Premium (Free Trial)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // The 4 Core YouTube Premium Perks
            item {
                Text(
                    text = "What's Included with Premium",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                PremiumPerkItem(
                    icon = Icons.Default.Block,
                    title = "Ad-free Videos",
                    description = "Watch millions of videos without video ads, banner popups, or interruptions.",
                    isUnlocked = isPremium
                )

                PremiumPerkItem(
                    icon = Icons.Default.PictureInPicture,
                    title = "Background Play & PiP",
                    description = "Keep videos and podcasts playing seamlessly while browsing other apps or when your screen is locked.",
                    isUnlocked = isPremium
                )

                PremiumPerkItem(
                    icon = Icons.Default.DownloadForOffline,
                    title = "Downloads & Offline Mode",
                    description = "Save full videos and shorts in up to 1080p and 4K Ultra HD for offline viewing anytime.",
                    isUnlocked = isPremium
                )

                PremiumPerkItem(
                    icon = Icons.Default.Headphones,
                    title = "INSANETUBE Music Premium",
                    description = "Uninterrupted audio listening mode, offline playlists, and zero audio commercial ads.",
                    isUnlocked = isPremium
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun PremiumPerkItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    isUnlocked: Boolean
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        border = BorderStroke(
            1.dp,
            if (isUnlocked) Color(0xFF6366F1).copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) Color(0xFF6366F1).copy(alpha = 0.18f)
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isUnlocked) Color(0xFF818CF8) else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (isUnlocked) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Active",
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
