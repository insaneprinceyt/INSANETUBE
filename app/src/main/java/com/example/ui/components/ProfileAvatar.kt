package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun ProfileAvatar(
    name: String,
    avatarColorHex: Long,
    avatarUri: String? = null,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    hasBlueTick: Boolean = false,
    isOwner: Boolean = false,
    isIncognito: Boolean = false,
    showEditBadge: Boolean = false,
    onEditClick: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val initial = name.trim().take(1).uppercase().ifEmpty { "?" }
    val baseColor = Color(avatarColorHex)

    val clickModifier = if (onClick != null) {
        Modifier.clickable { onClick() }
    } else Modifier

    Box(
        modifier = modifier
            .size(size)
            .then(clickModifier)
            .testTag("profile_avatar_${name.take(6).lowercase()}"),
        contentAlignment = Alignment.Center
    ) {
        // Border ring if owner or verified
        val borderModifier = when {
            isOwner -> Modifier.border(
                width = if (size > 60.dp) 3.dp else 2.dp,
                brush = Brush.sweepGradient(
                    listOf(
                        Color(0xFFF59E0B),
                        Color(0xFFFBBF24),
                        Color(0xFFD97706),
                        Color(0xFFF59E0B)
                    )
                ),
                shape = CircleShape
            )
            hasBlueTick -> Modifier.border(
                width = if (size > 60.dp) 3.dp else 2.dp,
                brush = Brush.sweepGradient(
                    listOf(
                        Color(0xFF0284C7),
                        Color(0xFF38BDF8),
                        Color(0xFF0369A1),
                        Color(0xFF0284C7)
                    )
                ),
                shape = CircleShape
            )
            else -> Modifier
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(borderModifier)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            when {
                isIncognito -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF27272A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🕶️",
                            fontSize = (size.value * 0.45f).sp
                        )
                    }
                }
                !avatarUri.isNullOrBlank() -> {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(avatarUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile picture for $name",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        loading = {
                            InitialFallback(initial = initial, backgroundColor = baseColor, size = size)
                        },
                        error = {
                            InitialFallback(initial = initial, backgroundColor = baseColor, size = size)
                        }
                    )
                }
                else -> {
                    InitialFallback(initial = initial, backgroundColor = baseColor, size = size)
                }
            }
        }

        // Camera / Edit Overlay Badge
        if (showEditBadge && onEditClick != null) {
            val badgeSize = (size * 0.34f).coerceIn(24.dp, 36.dp)
            val iconSize = (badgeSize * 0.58f).coerceIn(14.dp, 20.dp)
            Surface(
                onClick = onEditClick,
                shape = CircleShape,
                color = Color(0xFF0284C7),
                contentColor = Color.White,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .size(badgeSize)
                    .align(Alignment.BottomEnd)
                    .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                    .testTag("avatar_edit_badge_button")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Change profile picture",
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        }
    }
}

@Composable
private fun InitialFallback(
    initial: String,
    backgroundColor: Color,
    size: Dp
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        val fontSize = (size.value * 0.44f).sp
        Text(
            text = initial,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = fontSize
        )
    }
}
