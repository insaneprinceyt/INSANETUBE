package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.ui.components.ProfileAvatar
import com.example.ui.theme.InsaneRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelProfileScreen(
    channelAccount: UserAccountEntity,
    channelVideos: List<Video>,
    onBackClick: () -> Unit,
    onVideoClick: (Video) -> Unit,
    onOpenMonetization: () -> Unit,
    onOpenCustomize: () -> Unit,
    onOpenPremium: () -> Unit,
    onOpenChangeProfilePicture: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Home", "Videos", "Shorts", "Playlists", "Community", "About")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = channelAccount.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (channelAccount.hasBlueTick) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(17.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0284C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Verified Blue Tick",
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("channel_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (onOpenChangeProfilePicture != null) {
                        IconButton(onClick = onOpenChangeProfilePicture, modifier = Modifier.testTag("channel_top_photo_button")) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Change Profile Picture",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    IconButton(onClick = onOpenMonetization, modifier = Modifier.testTag("channel_top_monetization_button")) {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = "Monetization Studio",
                            tint = Color(0xFF10B981)
                        )
                    }
                    IconButton(onClick = onOpenPremium, modifier = Modifier.testTag("channel_top_premium_button")) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = "YouTube Premium",
                            tint = Color(0xFFF59E0B)
                        )
                    }
                    IconButton(onClick = onOpenCustomize, modifier = Modifier.testTag("channel_top_customize_button")) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Customize Channel"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        modifier = modifier
            .fillMaxSize()
            .testTag("channel_profile_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Channel Banner Header Art
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF0F172A),
                                    Color(0xFF1E3A8A),
                                    Color(0xFF0284C7),
                                    Color(0xFF090D16)
                                )
                            )
                        )
                ) {
                    // Decorative gaming/creator overlay banner
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 20.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "OFFICIAL PARTNER",
                            color = Color(0xFF38BDF8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = channelAccount.subscribers.uppercase(),
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            // Channel Identity & Metadata
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Large Avatar with Glow Border and Edit Badge
                        ProfileAvatar(
                            name = channelAccount.name,
                            avatarColorHex = channelAccount.avatarColorHex,
                            avatarUri = channelAccount.avatarUri,
                            size = 78.dp,
                            hasBlueTick = channelAccount.hasBlueTick,
                            isOwner = channelAccount.isOwner,
                            showEditBadge = onOpenChangeProfilePicture != null,
                            onEditClick = onOpenChangeProfilePicture,
                            onClick = onOpenChangeProfilePicture,
                            modifier = Modifier.testTag("channel_large_avatar")
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            // Name + Blue Tick + Owner Badge
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = channelAccount.name,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        fontSize = 20.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                // Blue Tick Badge
                                if (channelAccount.hasBlueTick) {
                                    Box(
                                        modifier = Modifier
                                            .size(19.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF0284C7)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Verified Blue Tick",
                                            tint = Color.White,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                            }

                            // Badges: OWNER + PREMIUM
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                if (channelAccount.isOwner) {
                                    Surface(
                                        color = Color(0xFFF59E0B).copy(alpha = 0.18f),
                                        shape = RoundedCornerShape(6.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.6f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "👑 OWNER",
                                                color = Color(0xFFF59E0B),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    }
                                }

                                if (channelAccount.isPremium) {
                                    Surface(
                                        color = InsaneRed.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(6.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, InsaneRed.copy(alpha = 0.5f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "⭐ PREMIUM",
                                                color = InsaneRed,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            // Handle, Subscribers & Videos
                            Text(
                                text = "${channelAccount.handle} • ${channelAccount.subscribers} • ${channelAccount.videosCount}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    // Bio
                    Text(
                        text = channelAccount.bio,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Channel Action Buttons (Manage videos, Monetization, Analytics)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onOpenCustomize,
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("channel_manage_videos_button")
                        ) {
                            Text(
                                text = "Manage videos",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Button(
                            onClick = onOpenMonetization,
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF047857),
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("channel_monetization_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachMoney,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Monetization",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(
                            onClick = onOpenPremium,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .testTag("channel_premium_icon_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = "Premium Perks",
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        if (onOpenChangeProfilePicture != null) {
                            IconButton(
                                onClick = onOpenChangeProfilePicture,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .testTag("channel_edit_photo_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Edit Profile Picture",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Channel Navigation Tabs (Home, Videos, Shorts, Playlists, Community, About)
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    edgePadding = 16.dp,
                    containerColor = Color.Transparent,
                    divider = {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        )
                    }
                }
            }

            // Tab Content
            when (selectedTabIndex) {
                0 -> {
                    // HOME TAB: Featured Spotlight & Video List
                    item {
                        ChannelHomeSection(
                            channelAccount = channelAccount,
                            videos = channelVideos,
                            onVideoClick = onVideoClick
                        )
                    }
                }
                1 -> {
                    // VIDEOS TAB: Complete Video Catalog
                    items(channelVideos) { vid ->
                        ChannelVideoRow(video = vid, onVideoClick = { onVideoClick(vid) })
                    }
                }
                2 -> {
                    // SHORTS TAB: Reels Grid
                    val shorts = channelVideos.filter { it.isShort }
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Shorts (${shorts.size})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                shorts.take(3).forEach { shortVid ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(9f / 16f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                Brush.verticalGradient(
                                                    listOf(
                                                        Color(shortVid.gradientStartHex),
                                                        Color(shortVid.gradientEndHex)
                                                    )
                                                )
                                            )
                                            .clickable { onVideoClick(shortVid) }
                                            .padding(8.dp)
                                    ) {
                                        Text(
                                            text = shortVid.views,
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.align(Alignment.BottomStart)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                3 -> {
                    // PLAYLISTS TAB
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            ChannelPlaylistItem(title = "🔥 Popular Uploads & Highlights", count = "45 videos")
                            ChannelPlaylistItem(title = "🎮 Pro Gameplay & Live Highlights", count = "128 videos")
                            ChannelPlaylistItem(title = "💻 Ultimate Tech Setups & Gear", count = "34 videos")
                            ChannelPlaylistItem(title = "🎙️ Creator Podcast & Vlogs", count = "18 videos")
                        }
                    }
                }
                4 -> {
                    // COMMUNITY TAB
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            CommunityPostCard(
                                channelName = channelAccount.name,
                                hasBlueTick = channelAccount.hasBlueTick,
                                avatarColor = channelAccount.avatarColorHex,
                                timeAgo = "2 days ago",
                                content = "18.5 MILLION SUBSCRIBERS! 🚀 Thank you guys for all the unbelievable love and support. Special giveaway stream happening this weekend!",
                                likes = "142K",
                                comments = "8.4K"
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            CommunityPostCard(
                                channelName = channelAccount.name,
                                hasBlueTick = channelAccount.hasBlueTick,
                                avatarColor = channelAccount.avatarColorHex,
                                timeAgo = "1 week ago",
                                content = "Poll: What setup video should we drop next? Unreal Engine 5.5 vs CryEngine or Custom Watercooled PC?",
                                likes = "98K",
                                comments = "5.1K"
                            )
                        }
                    }
                }
                5 -> {
                    // ABOUT TAB
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            Text(
                                text = channelAccount.bio,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                modifier = Modifier.padding(vertical = 16.dp)
                            )

                            Text(
                                text = "Channel Stats",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(bottom = 10.dp)
                            )

                            AboutStatRow(label = "Subscribers", value = channelAccount.subscribers)
                            AboutStatRow(label = "Total Views", value = channelAccount.totalViews)
                            AboutStatRow(label = "Total Uploads", value = channelAccount.videosCount)
                            AboutStatRow(label = "Joined INSANETUBE", value = "March 15, 2020")
                            AboutStatRow(label = "Status", value = "Official Partner • Monetization Active")
                            AboutStatRow(label = "Country", value = "Global / Worldwide")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChannelHomeSection(
    channelAccount: UserAccountEntity,
    videos: List<Video>,
    onVideoClick: (Video) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Featured Video Highlight
        if (videos.isNotEmpty()) {
            val featured = videos.first()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onVideoClick(featured) }
                    .padding(16.dp)
            ) {
                Text(
                    text = "FEATURED VIDEO",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF0284C7),
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(featured.gradientStartHex),
                                    Color(featured.gradientEndHex)
                                )
                            )
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier
                            .size(54.dp)
                            .align(Alignment.Center)
                    )

                    Surface(
                        color = Color.Black.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = featured.duration,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = featured.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 10.dp)
                )

                Text(
                    text = "${featured.views} • ${featured.timeAgo}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Popular Uploads Section Header
        Text(
            text = "Popular Uploads",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        videos.drop(1).take(5).forEach { vid ->
            ChannelVideoRow(video = vid, onVideoClick = { onVideoClick(vid) })
        }
    }
}

@Composable
private fun ChannelVideoRow(
    video: Video,
    onVideoClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onVideoClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(130.dp)
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(video.gradientStartHex),
                            Color(video.gradientEndHex)
                        )
                    )
                )
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.8f),
                shape = RoundedCornerShape(3.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
            ) {
                Text(
                    text = video.duration,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = video.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${video.views} • ${video.timeAgo}",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        IconButton(onClick = { /* video options */ }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun ChannelPlaylistItem(title: String, count: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(70.dp, 50.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF1E293B)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlaylistPlay,
                contentDescription = null,
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Text(
                text = count,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CommunityPostCard(
    channelName: String,
    hasBlueTick: Boolean,
    avatarColor: Long,
    timeAgo: String,
    content: String,
    likes: String,
    comments: String
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(avatarColor)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = channelName.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = channelName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        if (hasBlueTick) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0284C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = timeAgo,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = content,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.ThumbUp,
                        contentDescription = "Like",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = likes, fontSize = 12.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Comment,
                        contentDescription = "Comment",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = comments, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun AboutStatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
