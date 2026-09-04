package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_history")
data class WatchHistoryEntity(
    @PrimaryKey val videoId: String,
    val title: String,
    val channelName: String,
    val duration: String,
    val progressSeconds: Int,
    val durationSeconds: Int,
    val watchedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_videos")
data class SavedVideoEntity(
    @PrimaryKey val videoId: String,
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false,
    val isSavedWatchLater: Boolean = false,
    val isDownloaded: Boolean = false,
    val updatedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "channel_subscriptions")
data class SubscriptionEntity(
    @PrimaryKey val channelId: String,
    val isSubscribed: Boolean = true,
    val notificationEnabled: Boolean = true
)

@Entity(tableName = "user_comments")
data class UserCommentEntity(
    @PrimaryKey val commentId: String,
    val videoId: String,
    val authorName: String,
    val text: String,
    val likes: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_uploaded_videos")
data class UserUploadedVideoEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: String,
    val duration: String,
    val isShort: Boolean,
    val videoUri: String? = null,
    val isRealVideo: Boolean = true,
    val isFake: Boolean = false,
    val uploaderName: String = "Channel Member",
    val uploaderHandle: String = "@member",
    val soundTrack: String? = null,
    val tags: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "removed_videos")
data class RemovedVideoEntity(
    @PrimaryKey val videoId: String,
    val removedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_accounts")
data class UserAccountEntity(
    @PrimaryKey val id: String,
    val name: String,
    val handle: String,
    val email: String,
    val password: String = "password123",
    val avatarColorHex: Long = 0xFFFF0033,
    val avatarUri: String? = null,
    val avatarPreset: String? = null,
    val subscribers: String = "1.2K subscribers",
    val isVerified: Boolean = false,
    val hasBlueTick: Boolean = false,
    val isOwner: Boolean = false,
    val isPremium: Boolean = false,
    val videosCount: String = "1.2K videos",
    val totalViews: String = "1.8B views",
    val bio: String = "Official INSANETUBE Creator Channel • Content creator, gamer, tech enthusiast",
    val isCurrent: Boolean = false,
    val createdTimestamp: Long = System.currentTimeMillis()
)

