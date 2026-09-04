package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.ShortItem
import com.example.data.model.Video
import com.example.ui.components.CategoryChipsRow
import com.example.ui.components.ShortsShelf
import com.example.ui.components.VideoCard

@Composable
fun HomeScreen(
    videos: List<Video>,
    shorts: List<ShortItem>,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onVideoClick: (Video) -> Unit,
    onVideoMoreClick: (Video) -> Unit,
    onShortClick: (ShortItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen")
    ) {
        // Category Filter Chips
        CategoryChipsRow(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected
        )

        // Video Feed
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            itemsIndexed(videos, key = { _, video -> video.id }) { index, video ->
                VideoCard(
                    video = video,
                    onClick = { onVideoClick(video) },
                    onMoreClick = { onVideoMoreClick(video) }
                )

                // Inject YouTube Shorts shelf after the 2nd video
                if (index == 1 && shorts.isNotEmpty()) {
                    ShortsShelf(
                        shorts = shorts,
                        onShortClick = onShortClick
                    )
                }
            }

            // Bottom space for mini-player
            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}
