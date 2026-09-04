package com.example.data.model

data class Video(
    val id: String,
    val title: String,
    val channelId: String,
    val channelName: String,
    val channelAvatar: String,
    val subscriberCount: String,
    val verified: Boolean = true,
    val views: String,
    val viewCount: Long,
    val timeAgo: String,
    val duration: String,
    val durationSeconds: Int,
    val category: String, // "All", "Gaming", "Music", "Tech", "Podcasts", "Comedy", "Trending", "Live"
    val description: String,
    val likesCount: String,
    val likeNumber: Int,
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false,
    val isSavedWatchLater: Boolean = false,
    val isDownloaded: Boolean = false,
    val isSubscribed: Boolean = false,
    val isShort: Boolean = false,
    val tags: List<String> = emptyList(),
    val gradientStartHex: Long = 0xFF1E293B,
    val gradientEndHex: Long = 0xFF0F172A,
    val accentHex: Long = 0xFFFF0000,
    val commentsCount: Int = 1240,
    val videoUri: String? = null,
    val isRealVideo: Boolean = false,
    val isFake: Boolean = false
)

data class ShortItem(
    val id: String,
    val title: String,
    val channelName: String,
    val channelAvatar: String,
    val subscriberCount: String,
    val likesCount: String,
    val likeNumber: Int,
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false,
    val isSubscribed: Boolean = false,
    val commentsCount: String,
    val songTitle: String,
    val tags: List<String>,
    val gradientStartHex: Long,
    val gradientEndHex: Long,
    val videoUri: String? = null,
    val isReal: Boolean = false,
    val isFake: Boolean = false
)

data class Comment(
    val id: String,
    val videoId: String,
    val authorName: String,
    val authorAvatar: String,
    val text: String,
    val timeAgo: String,
    val likes: Int,
    val isLiked: Boolean = false,
    val isChannelOwner: Boolean = false
)

data class Channel(
    val id: String,
    val name: String,
    val handle: String,
    val avatarUrl: String,
    val subscribers: String,
    val videoCount: Int,
    val isSubscribed: Boolean = true,
    val hasUnreadVideos: Boolean = false
)

data class Playlist(
    val id: String,
    val title: String,
    val videoCount: Int,
    val isPrivate: Boolean = false,
    val iconName: String = "playlist",
    val description: String = ""
)

data class NotificationItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val channelName: String,
    val timeAgo: String,
    val isRead: Boolean = false,
    val videoId: String? = null
)

enum class NavTab(val title: String) {
    HOME("Home"),
    SHORTS("Shorts"),
    CREATE("Create"),
    SUBSCRIPTIONS("Subscriptions"),
    YOU("You")
}
