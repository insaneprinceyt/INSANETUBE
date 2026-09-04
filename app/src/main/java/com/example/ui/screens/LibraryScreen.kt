package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.data.local.WatchHistoryEntity
import com.example.data.model.Playlist
import com.example.data.model.Video
import com.example.ui.components.ProfileAvatar
import com.example.ui.theme.InsaneRed

@Composable
fun LibraryScreen(
    currentAccount: UserAccountEntity?,
    watchHistory: List<WatchHistoryEntity>,
    savedVideos: List<Video>,
    playlists: List<Playlist>,
    isIncognito: Boolean,
    onToggleIncognito: () -> Unit,
    onClearHistory: () -> Unit,
    onVideoClick: (Video) -> Unit,
    onSignInClick: () -> Unit,
    onSwitchAccountClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onViewChannelClick: () -> Unit = {},
    onMonetizationClick: () -> Unit = {},
    onPremiumClick: () -> Unit = {},
    onChangeProfilePictureClick: (() -> Unit)? = null,
    estimatedRevenue: Double = 84250.80,
    modifier: Modifier = Modifier
) {
    val downloadedVideos = savedVideos.filter { it.isDownloaded }
    val likedVideos = savedVideos.filter { it.isLiked }
    val watchLaterVideos = savedVideos.filter { it.isSavedWatchLater }
    val uploadedVideos = savedVideos.filter { it.channelId == "user_me" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("library_screen")
    ) {
        // User Profile Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (currentAccount != null || isIncognito) {
                    // LOGGED IN OR INCOGNITO
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = currentAccount != null && !isIncognito) {
                                onViewChannelClick()
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                ProfileAvatar(
                                    name = currentAccount?.name ?: "User",
                                    avatarColorHex = currentAccount?.avatarColorHex ?: 0xFFFF0033,
                                    avatarUri = currentAccount?.avatarUri,
                                    size = 64.dp,
                                    hasBlueTick = currentAccount?.hasBlueTick == true && !isIncognito,
                                    isOwner = currentAccount?.isOwner == true && !isIncognito,
                                    isIncognito = isIncognito,
                                    showEditBadge = currentAccount != null && !isIncognito && onChangeProfilePictureClick != null,
                                    onEditClick = onChangeProfilePictureClick,
                                    onClick = if (onChangeProfilePictureClick != null && !isIncognito) onChangeProfilePictureClick else onViewChannelClick,
                                    modifier = Modifier.testTag("library_profile_avatar")
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Text(
                                            text = if (isIncognito) "You're Incognito" else (currentAccount?.name ?: "User"),
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        if (currentAccount?.hasBlueTick == true && !isIncognito) {
                                            Box(
                                                modifier = Modifier
                                                    .size(16.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFF0284C7)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Verified Blue Tick",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(11.dp)
                                                )
                                            }
                                        } else if (currentAccount?.isVerified == true && !isIncognito) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Verified",
                                                tint = InsaneRed,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }

                                        if (currentAccount?.isOwner == true && !isIncognito) {
                                            Surface(
                                                color = Color(0xFFF59E0B).copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = "👑 OWNER",
                                                    color = Color(0xFFF59E0B),
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Black,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = if (isIncognito) "Watch history is paused" else "${currentAccount?.handle ?: "@user"} • ${currentAccount?.subscribers ?: "0 subscribers"}",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )

                                    if (currentAccount != null && !isIncognito) {
                                        Text(
                                            text = "View channel >",
                                            color = InsaneRed,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Action Chips (Your Channel, Monetization, Premium, Switch, Edit, Incognito, Sign out)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (currentAccount != null && !isIncognito) {
                                    ActionChip(
                                        icon = Icons.Outlined.AccountBox,
                                        label = "Your channel",
                                        onClick = onViewChannelClick,
                                        testTag = "your_channel_chip"
                                    )

                                    ActionChip(
                                        icon = Icons.Default.AttachMoney,
                                        label = "Monetization",
                                        onClick = onMonetizationClick,
                                        testTag = "monetization_chip"
                                    )

                                    ActionChip(
                                        icon = Icons.Default.Stars,
                                        label = if (currentAccount.isPremium) "Premium Active" else "Get Premium",
                                        onClick = onPremiumClick,
                                        testTag = "premium_chip"
                                    )
                                }

                                ActionChip(
                                    icon = Icons.Default.AccountCircle,
                                    label = "Switch account",
                                    onClick = onSwitchAccountClick,
                                    testTag = "switch_account_chip"
                                )

                                ActionChip(
                                    icon = Icons.Outlined.Edit,
                                    label = "Edit channel",
                                    onClick = onEditProfileClick,
                                    testTag = "edit_profile_chip"
                                )

                                if (currentAccount != null && !isIncognito && onChangeProfilePictureClick != null) {
                                    ActionChip(
                                        icon = Icons.Outlined.PhotoCamera,
                                        label = "Change photo",
                                        onClick = onChangeProfilePictureClick,
                                        testTag = "change_photo_chip"
                                    )
                                }

                                ActionChip(
                                    icon = if (isIncognito) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    label = if (isIncognito) "Turn off Incognito" else "Turn on Incognito",
                                    onClick = onToggleIncognito,
                                    testTag = "toggle_incognito_chip"
                                )

                                ActionChip(
                                    icon = Icons.Outlined.ExitToApp,
                                    label = "Sign out",
                                    onClick = onSignOutClick,
                                    testTag = "sign_out_chip"
                                )
                            }
                        }
                    }

                    // YouTube Premium & Monetization Quick Access Cards
                    if (currentAccount != null && !isIncognito) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // YouTube Premium Banner Card
                            Surface(
                                color = if (currentAccount.isPremium) Color(0xFF1E1B4B) else Color(0xFF18181B),
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(
                                    1.dp,
                                    if (currentAccount.isPremium) Color(0xFF6366F1).copy(alpha = 0.5f)
                                    else Color.White.copy(alpha = 0.1f)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onPremiumClick() }
                                    .testTag("premium_banner_card")
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Stars,
                                            contentDescription = null,
                                            tint = if (currentAccount.isPremium) Color(0xFF818CF8) else Color(0xFFF59E0B),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "PREMIUM",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 11.sp,
                                            letterSpacing = 1.sp,
                                            color = Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (currentAccount.isPremium) "Ad-free & Background active" else "Ad-free & Background play",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.7f),
                                        maxLines = 2
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = if (currentAccount.isPremium) "Manage Perks →" else "Upgrade Now →",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (currentAccount.isPremium) Color(0xFFA5B4FC) else InsaneRed
                                    )
                                }
                            }

                            // Creator Monetization Studio Banner Card
                            Surface(
                                color = Color(0xFF064E3B).copy(alpha = 0.4f),
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.35f)),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onMonetizationClick() }
                                    .testTag("monetization_banner_card")
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.AttachMoney,
                                            contentDescription = null,
                                            tint = Color(0xFF10B981),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "MONETIZATION",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 11.sp,
                                            letterSpacing = 1.sp,
                                            color = Color(0xFF10B981)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Est. Rev: $${"%,.2f".format(estimatedRevenue)}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Open Studio →",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF34D399)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // SIGNED OUT / GUEST STATE
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.AccountCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(54.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Enjoy your favorite videos",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = "Sign in to access videos that you’ve liked, saved, or uploaded, your subscriptions, and watch history.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = onSignInClick,
                                    colors = ButtonDefaults.buttonColors(containerColor = InsaneRed),
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .testTag("guest_signin_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Sign in", fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = onToggleIncognito,
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .testTag("guest_incognito_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Incognito", fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        }

        // History Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "History",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (watchHistory.isNotEmpty()) {
                            TextButton(onClick = onClearHistory) {
                                Text(
                                    text = "Clear",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        TextButton(onClick = { /* View all */ }) {
                            Text(
                                text = "View all",
                                color = Color(0xFF3B82F6),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                if (watchHistory.isEmpty()) {
                    Text(
                        text = "Videos you watch will show up here.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        watchHistory.take(8).forEach { item ->
                            val matchedVideo = savedVideos.find { it.id == item.videoId }
                            Column(
                                modifier = Modifier
                                    .width(140.dp)
                                    .clickable {
                                        if (matchedVideo != null) onVideoClick(matchedVideo)
                                    }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF1E293B)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color.White.copy(alpha = 0.8f),
                                        modifier = Modifier.size(24.dp)
                                    )

                                    // Duration in bottom right
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(4.dp)
                                            .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(3.dp))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = item.duration,
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = item.title,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 12.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 15.sp
                                )

                                Text(
                                    text = item.channelName,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        }

        // Playlists Section (Liked videos, Watch later, Custom playlists)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Playlists",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )

                    TextButton(onClick = { /* New Playlist */ }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "New playlist",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Liked Videos Card
                    PlaylistCard(
                        title = "Liked videos",
                        subtitle = "${likedVideos.size} videos",
                        icon = Icons.Default.ThumbUp,
                        gradientColors = listOf(Color(0xFF4C0519), Color(0xFF1E020A))
                    )

                    // Watch Later Card
                    PlaylistCard(
                        title = "Watch later",
                        subtitle = "${watchLaterVideos.size} videos",
                        icon = Icons.Default.WatchLater,
                        gradientColors = listOf(Color(0xFF1E1B4B), Color(0xFF0F0E26))
                    )

                    // Custom Playlists
                    playlists.forEach { pl ->
                        PlaylistCard(
                            title = pl.title,
                            subtitle = "${pl.videoCount} videos",
                            icon = Icons.Default.PlaylistPlay,
                            gradientColors = listOf(Color(0xFF1E293B), Color(0xFF0F172A))
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        }

        // Offline Downloads Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileDownloadDone,
                            contentDescription = null,
                            tint = Color(0xFF22C55E)
                        )
                        Text(
                            text = "Downloads (${downloadedVideos.size})",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                if (downloadedVideos.isEmpty()) {
                    Text(
                        text = "No downloaded videos yet. Tap download on any video to watch offline!",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                    downloadedVideos.forEach { dVideo ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onVideoClick(dVideo) }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(dVideo.gradientStartHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = dVideo.title,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${dVideo.channelName} • ${dVideo.duration}",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Ready offline",
                                tint = Color(0xFF22C55E),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        }

        // Your Videos (Uploaded)
        if (uploadedVideos.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Your videos (${uploadedVideos.size})",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    uploadedVideos.forEach { uVideo ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onVideoClick(uVideo) }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(InsaneRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = uVideo.title,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Uploaded just now • ${uVideo.category}",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            }
        }

        // Settings / Help rows
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                LibraryNavRow(icon = Icons.Outlined.Settings, label = "Settings")
                LibraryNavRow(icon = Icons.Outlined.HelpOutline, label = "Help and feedback")
            }
        }

        // Bottom space for mini-player
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun ActionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun PlaylistCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradientColors: List<Color>
) {
    Box(
        modifier = Modifier
            .width(130.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(androidx.compose.ui.graphics.Brush.verticalGradient(gradientColors))
            .padding(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.8f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(24.dp)
        )

        Column(modifier = Modifier.align(Alignment.BottomStart)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun LibraryNavRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Settings click */ }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp
        )
    }
}
