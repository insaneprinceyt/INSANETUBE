package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
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
import com.example.data.model.ShortItem
import com.example.ui.theme.InsaneRed

@Composable
fun ShortsShelf(
    shorts: List<ShortItem>,
    onShortClick: (ShortItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .testTag("shorts_shelf")
    ) {
        // Shelf Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "Shorts",
                    tint = InsaneRed,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Shorts",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Horizontal List of Shorts Cards (9:16 aspect ratio cards)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            shorts.forEach { shortItem ->
                ShortCard(
                    shortItem = shortItem,
                    onClick = { onShortClick(shortItem) }
                )
            }
        }
    }
}

@Composable
private fun ShortCard(
    shortItem: ShortItem,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(150.dp)
            .height(260.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(shortItem.gradientStartHex),
                        Color(shortItem.gradientEndHex)
                    )
                )
            )
            .clickable(onClick = onClick)
            .testTag("short_card_${shortItem.id}")
    ) {
        // Decorative center icon
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(44.dp)
                .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(22.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play Short",
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.size(28.dp)
            )
        }

        // Top-right 3-dot
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Options",
            tint = Color.White.copy(alpha = 0.8f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(18.dp)
        )

        // Bottom text overlay with gradient
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                    )
                )
                .padding(10.dp)
        ) {
            Text(
                text = shortItem.title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${shortItem.likesCount} views",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
