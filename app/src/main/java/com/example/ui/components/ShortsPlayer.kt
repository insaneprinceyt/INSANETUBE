package com.example.ui.components

import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.ShortItem
import com.example.ui.theme.InsaneRed

@Composable
fun ShortsPlayerScreen(
    shorts: List<ShortItem>,
    currentIndex: Int,
    onNextShort: () -> Unit,
    onPrevShort: () -> Unit,
    onOpenComments: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (shorts.isEmpty()) return
    val short = shorts.getOrElse(currentIndex) { shorts.first() }

    var isPaused by remember { mutableStateOf(false) }
    var isLiked by remember(short.id) { mutableStateOf(short.isLiked) }
    var isDisliked by remember(short.id) { mutableStateOf(short.isDisliked) }
    var isSubscribed by remember(short.id) { mutableStateOf(short.isSubscribed) }

    // Rotating vinyl disc animation
    val infiniteTransition = rememberInfiniteTransition(label = "disc_spin")
    val discRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "disc_rotation"
    )

    // Progress bar loop
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shorts_progress"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                var totalDrag = 0f
                detectVerticalDragGestures(
                    onDragStart = { totalDrag = 0f },
                    onDragEnd = {
                        if (totalDrag < -100f) {
                            onNextShort()
                        } else if (totalDrag > 100f) {
                            onPrevShort()
                        }
                    },
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        totalDrag += dragAmount
                    }
                )
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPaused = !isPaused
            }
            .testTag("shorts_player_screen")
    ) {
        // Video Visual Surface (Real video playback OR Dynamic Gradient visualizer)
        if (short.videoUri != null) {
            AndroidView(
                factory = { ctx ->
                    VideoView(ctx).apply {
                        setVideoURI(Uri.parse(short.videoUri))
                        setOnPreparedListener { mp ->
                            mp.isLooping = true
                            if (!isPaused) start()
                        }
                    }
                },
                update = { vv ->
                    if (!isPaused && !vv.isPlaying) {
                        vv.start()
                    } else if (isPaused && vv.isPlaying) {
                        vv.pause()
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
                                Color(short.gradientStartHex),
                                Color(short.gradientEndHex),
                                Color.Black
                            )
                        )
                    )
            ) {
                // Ambient Graphic Center
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Bolt,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (short.isReal) "REAL MEMBER REEL" else "INSANETUBE DEMO REEL",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }

                // Pause Indicator Overlay
                if (isPaused) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Pause,
                            contentDescription = "Paused",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        }

        // Top App Bar Controls (Shorts title, Camera, Search, Options)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Shorts",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = { /* Search */ }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { /* Camera / Record */ }) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "Create Short",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { /* More options */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = Color.White
                    )
                }
            }
        }

        // Bottom Details & Creator Info (Bottom Left)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.78f)
                .padding(start = 16.dp, bottom = 28.dp)
        ) {
            // Channel row with Subscribe button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE50914)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = short.channelName.take(1),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Text(
                    text = short.channelName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                // Subscribe Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSubscribed) Color.White.copy(alpha = 0.25f) else InsaneRed)
                        .clickable { isSubscribed = !isSubscribed }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("shorts_subscribe_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isSubscribed) "Subscribed" else "Subscribe",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Video Title
            Text(
                text = short.title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tags
            Text(
                text = short.tags.joinToString(" "),
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sound / Audio info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = short.songTitle,
                    color = Color.White,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Right Vertical Actions Column (Like, Dislike, Comments, Share, Remix, Vinyl)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Like
            ShortActionButton(
                icon = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                label = if (isLiked) "Liked" else short.likesCount,
                tint = if (isLiked) InsaneRed else Color.White,
                onClick = {
                    isLiked = !isLiked
                    if (isLiked) isDisliked = false
                },
                testTag = "short_like_button"
            )

            // Dislike
            ShortActionButton(
                icon = if (isDisliked) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                label = "Dislike",
                tint = if (isDisliked) InsaneRed else Color.White,
                onClick = {
                    isDisliked = !isDisliked
                    if (isDisliked) isLiked = false
                },
                testTag = "short_dislike_button"
            )

            // Comments
            ShortActionButton(
                icon = Icons.Outlined.Comment,
                label = short.commentsCount,
                tint = Color.White,
                onClick = onOpenComments,
                testTag = "short_comments_button"
            )

            // Share
            ShortActionButton(
                icon = Icons.Outlined.Share,
                label = "Share",
                tint = Color.White,
                onClick = onShareClick,
                testTag = "short_share_button"
            )

            // Remix
            ShortActionButton(
                icon = Icons.Outlined.AutoAwesome,
                label = "Remix",
                tint = Color.White,
                onClick = { /* Remix action */ },
                testTag = "short_remix_button"
            )

            // Rotating Vinyl Sound Disc
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .rotate(if (isPaused) 0f else discRotation)
                    .clip(CircleShape)
                    .background(Color(0xFF222222))
                    .border(2.dp, Color.White.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(InsaneRed)
                )
            }
        }

        // Looping progress bar at bottom
        LinearProgressIndicator(
            progress = { if (isPaused) 0.5f else progress },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(2.5.dp),
            color = InsaneRed,
            trackColor = Color.White.copy(alpha = 0.2f)
        )
    }
}

@Composable
private fun ShortActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.35f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
