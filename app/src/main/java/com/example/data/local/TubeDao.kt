package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TubeDao {
    // Watch History
    @Query("SELECT * FROM watch_history ORDER BY watchedTimestamp DESC")
    fun getWatchHistory(): Flow<List<WatchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordWatchHistory(item: WatchHistoryEntity)

    @Query("DELETE FROM watch_history WHERE videoId = :videoId")
    suspend fun removeFromHistory(videoId: String)

    @Query("DELETE FROM watch_history")
    suspend fun clearHistory()

    // Saved Videos (Likes, Watch Later, Downloads)
    @Query("SELECT * FROM saved_videos")
    fun getAllSavedVideos(): Flow<List<SavedVideoEntity>>

    @Query("SELECT * FROM saved_videos WHERE videoId = :videoId")
    suspend fun getSavedVideo(videoId: String): SavedVideoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSavedVideo(savedVideo: SavedVideoEntity)

    // Subscriptions
    @Query("SELECT * FROM channel_subscriptions")
    fun getSubscriptions(): Flow<List<SubscriptionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSubscription(sub: SubscriptionEntity)

    // User Comments
    @Query("SELECT * FROM user_comments WHERE videoId = :videoId ORDER BY timestamp DESC")
    fun getCommentsForVideo(videoId: String): Flow<List<UserCommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: UserCommentEntity)

    // User Uploaded Videos
    @Query("SELECT * FROM user_uploaded_videos ORDER BY timestamp DESC")
    fun getUserUploadedVideos(): Flow<List<UserUploadedVideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUploadedVideo(video: UserUploadedVideoEntity)

    @Query("DELETE FROM user_uploaded_videos WHERE id = :id")
    suspend fun deleteUploadedVideo(id: String)

    // Removed / Filtered Fake Videos Moderation
    @Query("SELECT videoId FROM removed_videos")
    fun getRemovedVideoIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markVideoRemoved(entity: RemovedVideoEntity)

    @Query("DELETE FROM removed_videos WHERE videoId = :videoId")
    suspend fun restoreVideo(videoId: String)

    @Query("DELETE FROM removed_videos")
    suspend fun clearRemovedVideos()

    // User Accounts & Authentication
    @Query("SELECT * FROM user_accounts ORDER BY createdTimestamp ASC")
    fun getAllAccounts(): Flow<List<UserAccountEntity>>

    @Query("SELECT * FROM user_accounts WHERE isCurrent = 1 LIMIT 1")
    fun getCurrentAccount(): Flow<UserAccountEntity?>

    @Query("SELECT * FROM user_accounts WHERE isCurrent = 1 LIMIT 1")
    suspend fun getCurrentAccountDirect(): UserAccountEntity?

    @Query("SELECT * FROM user_accounts WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getAccountByEmail(email: String): UserAccountEntity?

    @Query("SELECT * FROM user_accounts WHERE LOWER(handle) = LOWER(:handle) LIMIT 1")
    suspend fun getAccountByHandle(handle: String): UserAccountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: UserAccountEntity)

    @Query("UPDATE user_accounts SET isCurrent = 0")
    suspend fun clearCurrentAccount()

    @Query("UPDATE user_accounts SET isCurrent = 1 WHERE id = :id")
    suspend fun setCurrentAccount(id: String)

    @Query("UPDATE user_accounts SET name = :name, handle = :handle WHERE id = :id")
    suspend fun updateProfile(id: String, name: String, handle: String)

    @Query("UPDATE user_accounts SET avatarUri = :avatarUri WHERE id = :id")
    suspend fun updateAvatarUri(id: String, avatarUri: String?)

    @Query("UPDATE user_accounts SET avatarColorHex = :avatarColorHex WHERE id = :id")
    suspend fun updateAvatarColor(id: String, avatarColorHex: Long)

    @Query("UPDATE user_accounts SET avatarUri = :avatarUri, avatarColorHex = :avatarColorHex WHERE id = :id")
    suspend fun updateAvatarPhotoAndColor(id: String, avatarUri: String?, avatarColorHex: Long)

    @Query("UPDATE user_accounts SET isPremium = :isPremium WHERE id = :id")
    suspend fun updatePremiumStatus(id: String, isPremium: Boolean)

    @Query("DELETE FROM user_accounts WHERE id = :id")
    suspend fun deleteAccount(id: String)
}
