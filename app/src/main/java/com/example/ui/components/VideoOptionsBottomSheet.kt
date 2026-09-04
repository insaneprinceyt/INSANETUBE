package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Video
import com.example.ui.theme.InsaneRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoOptionsBottomSheet(
    video: Video?,
    onDismiss: () -> Unit,
    onToggleWatchLater: (Video) -> Unit,
    onToggleDownload: (Video) -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (video == null) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.testTag("video_options_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = video.title,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            // Save to Watch later
            MenuRow(
                icon = if (video.isSavedWatchLater) Icons.Filled.WatchLater else Icons.Outlined.WatchLater,
                tint = if (video.isSavedWatchLater) InsaneRed else MaterialTheme.colorScheme.onSurfaceVariant,
                label = if (video.isSavedWatchLater) "Remove from Watch later" else "Save to Watch later",
                onClick = {
                    onToggleWatchLater(video)
                    onDismiss()
                },
                testTag = "option_watch_later"
            )

            // Save to playlist
            MenuRow(
                icon = Icons.Outlined.PlaylistAdd,
                label = "Save to playlist",
                onClick = onDismiss,
                testTag = "option_playlist"
            )

            // Download video
            MenuRow(
                icon = if (video.isDownloaded) Icons.Filled.DownloadDone else Icons.Outlined.Download,
                tint = if (video.isDownloaded) InsaneRed else MaterialTheme.colorScheme.onSurfaceVariant,
                label = if (video.isDownloaded) "Delete download" else "Download video",
                onClick = {
                    onToggleDownload(video)
                    onDismiss()
                },
                testTag = "option_download"
            )

            // Share
            MenuRow(
                icon = Icons.Outlined.Share,
                label = "Share",
                onClick = {
                    onShareClick()
                    onDismiss()
                },
                testTag = "option_share"
            )

            // Not interested
            MenuRow(
                icon = Icons.Outlined.NotInterested,
                label = "Not interested",
                onClick = onDismiss,
                testTag = "option_not_interested"
            )

            // Don't recommend channel
            MenuRow(
                icon = Icons.Outlined.DoNotDisturbOn,
                label = "Don't recommend channel",
                onClick = onDismiss,
                testTag = "option_dont_recommend"
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MenuRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp
        )
    }
}
