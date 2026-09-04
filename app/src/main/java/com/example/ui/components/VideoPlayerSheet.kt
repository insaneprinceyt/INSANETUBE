package com.example.ui.components

import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.Video
import com.example.ui.theme.InsaneRed
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerSheet(
    video: Video?,
    isExpanded: Boolean,
    isPlaying: Boolean,
    currentSeconds: Int,
    playbackSpeed: String,
    videoQuality: String,
    captionsEnabled: Boolean,
    autoPlay: Boolean,
    relatedVideos: List<Video>,
    onTogglePlay: () -> Unit,
    onSeekTo: (Int) -> Unit,
    onSeekRelative: (Int) -> Unit,
    onPlayNext: () -> Unit,
    onPlayPrev: () -> Unit,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    onClose: () -> Unit,
    onToggleLike: () -> Unit,
    onToggleDislike: () -> Unit,
    onToggleWatchLater: (Video) -> Unit,
    onToggleDownload: (Video) -> Unit,
    onToggleSubscribe: (String, Boolean) -> Unit,
    onOpenComments: () -> Unit,
    onShareClick: () -> Unit,
    onCycleSpeed: () -> Unit,
    onCycleQuality: () -> Unit,
    onToggleCaptions: () -> Unit,
    onToggleAutoPlay: () -> Unit,
    onVideoClick: (Video) -> Unit,
    modifier: Modifier = Modifier
) {
    if (video == null) return

    if (!isExpanded) {
        // Floating Mini-Player Bar (docked above bottom navigation)
        MiniPlayerBar(
            video = video,
            isPlaying = isPlaying,
            currentSeconds = currentSeconds,
            onExpand = onExpand,
            onTogglePlay = onTogglePlay,
            onClose = onClose,
            modifier = modifier
        )
    } else {
        // Full-screen Video Player & Feed
        FullVideoPlayer(
            video = video,
            isPlaying = isPlaying,
            currentSeconds = currentSeconds,
            playbackSpeed = playbackSpeed,
            videoQuality = videoQuality,
            captionsEnabled = captionsEnabled,
            autoPlay = autoPlay,
            relatedVideos = relatedVideos,
            onTogglePlay = onTogglePlay,
            onSeekTo = onSeekTo,
            onSeekRelative = onSeekRelative,
            onPlayNext = onPlayNext,
            onPlayPrev = onPlayPrev,
            onCollapse = onCollapse,
            onToggleLike = onToggleLike,
            onToggleDislike = onToggleDislike,
            onToggleWatchLater = onToggleWatchLater,
            onToggleDownload = onToggleDownload,
            onToggleSubscribe = onToggleSubscribe,
            onOpenComments = onOpenComments,
            onShareClick = onShareClick,
            onCycleSpeed = onCycleSpeed,
            onCycleQuality = onCycleQuality,
            onToggleCaptions = onToggleCaptions,
            onToggleAutoPlay = onToggleAutoPlay,
            onVideoClick = onVideoClick,
            modifier = modifier
        )
    }
}

@Composable
private fun MiniPlayerBar(
    video: Video,
    isPlaying: Boolean,
    currentSeconds: Int,
    onExpand: () -> Unit,
    onTogglePlay: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onExpand)
            .testTag("mini_player_bar")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Mini progress line
            val totalSec = if (video.durationSeconds > 0) video.durationSeconds else 300
            val progressFrac = (currentSeconds.toFloat() / totalSec).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progressFrac },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = InsaneRed,
                trackColor = Color.Transparent
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mini 16:9 thumbnail preview
                Box(
                    modifier = Modifier
                        .width(72.dp)
                        .height(42.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(video.gradientStartHex), Color(video.gradientEndHex))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title & Channel
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = video.title,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = video.channelName,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }

                // Play / Pause Button
                IconButton(
                    onClick = onTogglePlay,
                    modifier = Modifier.testTag("mini_player_play_pause")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Close Button
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("mini_player_close")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FullVideoPlayer(
    video: Video,
    isPlaying: Boolean,
    currentSeconds: Int,
    playbackSpeed: String,
    videoQuality: String,
    captionsEnabled: Boolean,
    autoPlay: Boolean,
    relatedVideos: List<Video>,
    onTogglePlay: () -> Unit,
    onSeekTo: (Int) -> Unit,
    onSeekRelative: (Int) -> Unit,
    onPlayNext: () -> Unit,
    onPlayPrev: () -> Unit,
    onCollapse: () -> Unit,
    onToggleLike: () -> Unit,
    onToggleDislike: () -> Unit,
    onToggleWatchLater: (Video) -> Unit,
    onToggleDownload: (Video) -> Unit,
    onToggleSubscribe: (String, Boolean) -> Unit,
    onOpenComments: () -> Unit,
    onShareClick: () -> Unit,
    onCycleSpeed: () -> Unit,
    onCycleQuality: () -> Unit,
    onToggleCaptions: () -> Unit,
    onToggleAutoPlay: () -> Unit,
    onVideoClick: (Video) -> Unit,
    modifier: Modifier = Modifier
) {
    var showControls by remember { mutableStateOf(true) }
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    // Auto-hide controls after 4 seconds of inactivity if playing
    LaunchedEffect(showControls, isPlaying) {
        if (showControls && isPlaying) {
            delay(4000)
            showControls = false
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Status bar spacer for video header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .background(Color.Black)
            )

            // Video Player Viewport (16:9)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(Color.Black)
                    .clickable { showControls = !showControls }
                    .testTag("video_player_viewport")
            ) {
                // Real Video View or Background visualizer / dynamic cinematic gradient
                if (video.videoUri != null) {
                    AndroidView(
                        factory = { ctx ->
                            VideoView(ctx).apply {
                                setVideoURI(Uri.parse(video.videoUri))
                                setOnPreparedListener { mp ->
                                    mp.isLooping = true
                                    if (isPlaying) start()
                                }
                            }
                        },
                        update = { view ->
                            if (isPlaying && !view.isPlaying) {
                                view.start()
                            } else if (!isPlaying && view.isPlaying) {
                                view.pause()
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(video.gradientStartHex),
                                        Color(video.gradientEndHex)
                                    )
                                )
                            )
                    ) {
                        // Animated audio bars in center representing live stream / playing video
                        if (isPlaying) {
                            AnimatedAudioBars(
                                color = Color(video.accentHex).copy(alpha = 0.5f),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }

                // Real Member Video Badge
                if (video.isRealVideo || video.videoUri != null) {
                    Surface(
                        color = Color(0xFF10B981).copy(alpha = 0.85f),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("REAL VIDEO", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Closed Captions banner
                if (captionsEnabled) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 36.dp)
                            .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "[${video.channelName}]: \"In this chapter, let's explore the inner system architecture...\"",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Player Controls Overlay
                androidx.compose.animation.AnimatedVisibility(
                    visible = showControls,
                    enter = fadeIn(tween(150)),
                    exit = fadeOut(tween(150))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.55f))
                    ) {
                        // Top Bar in Player Overlay
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .align(Alignment.TopCenter),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Minimize button
                            IconButton(onClick = onCollapse) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Minimize player",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                // AutoPlay toggle
                                IconButton(onClick = onToggleAutoPlay) {
                                    Icon(
                                        imageVector = if (autoPlay) Icons.Default.Autorenew else Icons.Default.PauseCircleFilled,
                                        contentDescription = "Auto play",
                                        tint = if (autoPlay) InsaneRed else Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                // Captions CC toggle
                                IconButton(onClick = onToggleCaptions) {
                                    Icon(
                                        imageVector = Icons.Default.ClosedCaption,
                                        contentDescription = "Captions",
                                        tint = if (captionsEnabled) InsaneRed else Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                // Quality selector
                                TextButton(onClick = onCycleQuality) {
                                    Text(
                                        text = videoQuality,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // Speed selector
                                TextButton(onClick = onCycleSpeed) {
                                    Text(
                                        text = playbackSpeed,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Center Controls: Rewind 10, Play/Pause, Fast Forward 10
                        Row(
                            modifier = Modifier.align(Alignment.Center),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            // Prev Video
                            IconButton(onClick = onPlayPrev) {
                                Icon(
                                    imageVector = Icons.Default.SkipPrevious,
                                    contentDescription = "Previous Video",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            // 10s Rewind
                            IconButton(onClick = { onSeekRelative(-10) }) {
                                Icon(
                                    imageVector = Icons.Default.Replay10,
                                    contentDescription = "Rewind 10s",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            // Large Play / Pause button
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.5f))
                                    .clickable(onClick = onTogglePlay)
                                    .testTag("player_play_pause_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Pause" else "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp)
                                )
                            }

                            // 10s Fast Forward
                            IconButton(onClick = { onSeekRelative(10) }) {
                                Icon(
                                    imageVector = Icons.Default.Forward10,
                                    contentDescription = "Forward 10s",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            // Next Video
                            IconButton(onClick = onPlayNext) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = "Next Video",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        // Bottom Scrub Bar & Time display
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val totalSec = if (video.durationSeconds > 0) video.durationSeconds else 300
                                val curMin = currentSeconds / 60
                                val curSec = currentSeconds % 60
                                val totMin = totalSec / 60
                                val totSec = totalSec % 60
                                val timeText = String.format("%02d:%02d / %02d:%02d", curMin, curSec, totMin, totSec)

                                Text(
                                    text = timeText,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                Icon(
                                    imageVector = Icons.Default.Fullscreen,
                                    contentDescription = "Fullscreen",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Red Scrub Slider
                            val totalSeconds = if (video.durationSeconds > 0) video.durationSeconds else 300
                            Slider(
                                value = currentSeconds.toFloat(),
                                onValueChange = { onSeekTo(it.toInt()) },
                                valueRange = 0f..totalSeconds.toFloat(),
                                colors = SliderDefaults.colors(
                                    thumbColor = InsaneRed,
                                    activeTrackColor = InsaneRed,
                                    inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(24.dp)
                            )
                        }
                    }
                }
            }

            // Scrollable Content Below Video (Title, Channel, Actions, Comments, Up Next)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Video Title & Meta
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = video.title,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                lineHeight = 22.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Views & Date & Description expandable box
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isDescriptionExpanded = !isDescriptionExpanded }
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "${video.views}  ${video.timeAgo}",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (isDescriptionExpanded) "Show less" else "...more",
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (isDescriptionExpanded) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = video.description,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 12.sp,
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Channel Info Row with Subscribe Button
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(video.accentHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = video.channelName.take(1).uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }

                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = video.channelName,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    if (video.verified) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Verified",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = video.subscriberCount,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Subscribe Pill
                        val isSubscribed = video.isSubscribed
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSubscribed) MaterialTheme.colorScheme.surfaceVariant
                                    else InsaneRed
                                )
                                .clickable { onToggleSubscribe(video.channelId, isSubscribed) }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("player_subscribe_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (isSubscribed) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsActive,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = if (isSubscribed) "Subscribed" else "Subscribe",
                                    color = if (isSubscribed) MaterialTheme.colorScheme.onSurfaceVariant else Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Action Pills Carousel (Like/Dislike, Share, Download, Watch Later, Save)
                item {
                    val pillScrollState = rememberScrollState()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(pillScrollState)
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Joined Like / Dislike Pill
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                // Like button
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clickable(onClick = onToggleLike)
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                        .testTag("player_like_button"),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = if (video.isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                        contentDescription = "Like",
                                        tint = if (video.isLiked) InsaneRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = video.likesCount,
                                        color = if (video.isLiked) InsaneRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                VerticalDivider(
                                    modifier = Modifier
                                        .height(18.dp)
                                        .width(1.dp),
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                )

                                // Dislike button
                                IconButton(
                                    onClick = onToggleDislike,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("player_dislike_button")
                                ) {
                                    Icon(
                                        imageVector = if (video.isDisliked) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                                        contentDescription = "Dislike",
                                        tint = if (video.isDisliked) InsaneRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        // Share Pill
                        ActionPill(
                            icon = Icons.Outlined.Share,
                            label = "Share",
                            onClick = onShareClick,
                            testTag = "player_share_pill"
                        )

                        // Download Pill
                        ActionPill(
                            icon = if (video.isDownloaded) Icons.Filled.DownloadDone else Icons.Outlined.Download,
                            label = if (video.isDownloaded) "Downloaded" else "Download",
                            tint = if (video.isDownloaded) InsaneRed else MaterialTheme.colorScheme.onSurfaceVariant,
                            onClick = { onToggleDownload(video) },
                            testTag = "player_download_pill"
                        )

                        // Watch Later Pill
                        ActionPill(
                            icon = if (video.isSavedWatchLater) Icons.Filled.WatchLater else Icons.Outlined.WatchLater,
                            label = if (video.isSavedWatchLater) "Saved" else "Watch Later",
                            tint = if (video.isSavedWatchLater) InsaneRed else MaterialTheme.colorScheme.onSurfaceVariant,
                            onClick = { onToggleWatchLater(video) },
                            testTag = "player_watch_later_pill"
                        )

                        // Remix Pill
                        ActionPill(
                            icon = Icons.Outlined.AutoAwesome,
                            label = "Remix",
                            onClick = { /* Remix action */ },
                            testTag = "player_remix_pill"
                        )
                    }
                }

                // Comments Preview Section
                item {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .clickable(onClick = onOpenComments)
                            .testTag("player_comments_preview")
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Comments",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${video.commentsCount}",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(InsaneRed),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "S",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = "The pacing on this video was absolute perfection! Watched from start to finish 🔥",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                // Up Next Section Header
                item {
                    Text(
                        text = "Up next",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }

                // Up Next Videos
                items(relatedVideos.filter { it.id != video.id }) { related ->
                    VideoCard(
                        video = related,
                        onClick = { onVideoClick(related) },
                        onMoreClick = { /* More options */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = label,
                color = tint,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AnimatedAudioBars(
    color: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "audio_bars")
    val bar1 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(400, easing = LinearEasing), RepeatMode.Reverse),
        label = "b1"
    )
    val bar2 by infiniteTransition.animateFloat(
        initialValue = 0.8f, targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(350, easing = LinearEasing), RepeatMode.Reverse),
        label = "b2"
    )
    val bar3 by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(450, easing = LinearEasing), RepeatMode.Reverse),
        label = "b3"
    )

    Row(
        modifier = modifier.height(36.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(bar1, bar2, bar3, bar2, bar1).forEach { heightFraction ->
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight(heightFraction)
                    .clip(RoundedCornerShape(2.dp))
                    .background(color)
            )
        }
    }
}
