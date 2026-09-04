package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class TubeRepository(private val dao: TubeDao) {

    // Initial rich YouTube video catalog
    private val baseVideos = listOf(
        Video(
            id = "v1",
            title = "Building an OS in 24 Hours - INSANE Engineering Challenge",
            channelId = "c1",
            channelName = "Tech Insane",
            channelAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=120",
            subscriberCount = "4.82M subscribers",
            verified = true,
            views = "1.8M views",
            viewCount = 1840000L,
            timeAgo = "3 hours ago",
            duration = "21:45",
            durationSeconds = 1305,
            category = "Tech",
            description = "Can we build a bootable x86 operating system from scratch in just 24 hours? We write a custom bootloader, kernel, memory manager, and basic GUI shell. Hit subscribe for more INSANE coding journeys!\n\n#tech #coding #operatingSystem #INSANETUBE",
            likesCount = "142K",
            likeNumber = 142000,
            tags = listOf("Coding", "Operating Systems", "Kernel", "Hardware"),
            gradientStartHex = 0xFF0D1B2A,
            gradientEndHex = 0xFF1B263B,
            accentHex = 0xFF00E5FF,
            commentsCount = 2840
        ),
        Video(
            id = "v2",
            title = "Top 10 INSANE Unreal Engine 5.5 Next-Gen Visuals",
            channelId = "c2",
            channelName = "GamerNexus Pro",
            channelAvatar = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=120",
            subscriberCount = "2.1M subscribers",
            verified = true,
            views = "840K views",
            viewCount = 840000L,
            timeAgo = "1 day ago",
            duration = "14:12",
            durationSeconds = 852,
            category = "Gaming",
            description = "Unreal Engine 5.5 has officially launched with Nanite dynamic tessellation and MegaLights! Here are the 10 most mind-blowing demonstrations showcasing real-time photorealism running on consumer GPUs.",
            likesCount = "68K",
            likeNumber = 68000,
            tags = listOf("Gaming", "UnrealEngine5", "RTX5090", "NextGen"),
            gradientStartHex = 0xFF2B0938,
            gradientEndHex = 0xFF14051E,
            accentHex = 0xFFBD00FF,
            commentsCount = 1410
        ),
        Video(
            id = "v3",
            title = "Chill Lofi Beats to Relax / Study / Code to [24/7 Live Stream]",
            channelId = "c3",
            channelName = "Lofi Insane Girl",
            channelAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=120",
            subscriberCount = "12.4M subscribers",
            verified = true,
            views = "3.2M views",
            viewCount = 3200000L,
            timeAgo = "Streaming now",
            duration = "LIVE",
            durationSeconds = 99999,
            category = "Music",
            description = "Welcome to the 24/7 Insane Beats stream. Grab your headphones, tea or coffee, and enjoy smooth ambient vibes.\n\nTracklist continuously updated.",
            likesCount = "395K",
            likeNumber = 395000,
            tags = listOf("Lofi", "Chill", "StudyMusic", "Beats"),
            gradientStartHex = 0xFF3D1308,
            gradientEndHex = 0xFF1A0803,
            accentHex = 0xFFFF7A00,
            commentsCount = 9250
        ),
        Video(
            id = "v4",
            title = "I Survived 100 Days in a High-Tech Underground Bunker",
            channelId = "c4",
            channelName = "Apex Explorers",
            channelAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=120",
            subscriberCount = "8.9M subscribers",
            verified = true,
            views = "5.6M views",
            viewCount = 5600000L,
            timeAgo = "4 days ago",
            duration = "32:10",
            durationSeconds = 1930,
            category = "Trending",
            description = "We spent 100 continuous days deep subterranean in a fully self-sufficient eco-bunker equipped with hydroponic gardens, atmospheric water generators, and robotic defense systems. What happened on day 78 was shocking!",
            likesCount = "480K",
            likeNumber = 480000,
            tags = listOf("Survival", "Challenge", "Bunker", "100Days"),
            gradientStartHex = 0xFF1A3320,
            gradientEndHex = 0xFF0D1A10,
            accentHex = 0xFF00FF66,
            commentsCount = 4920
        ),
        Video(
            id = "v5",
            title = "Full Android 16 Jetpack Compose Masterclass 2026",
            channelId = "c1",
            channelName = "Tech Insane",
            channelAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=120",
            subscriberCount = "4.82M subscribers",
            verified = true,
            views = "490K views",
            viewCount = 490000L,
            timeAgo = "1 week ago",
            duration = "1:15:30",
            durationSeconds = 4530,
            category = "Tech",
            description = "Master Modern Android Development in 2026: Material 3, Clean Architecture, Kotlin Coroutines, StateFlow, Room Database, and Navigation Compose.",
            likesCount = "42K",
            likeNumber = 42000,
            tags = listOf("Android", "Kotlin", "JetpackCompose", "MobileDev"),
            gradientStartHex = 0xFF0A2540,
            gradientEndHex = 0xFF051320,
            accentHex = 0xFF3DDC84,
            commentsCount = 1180
        ),
        Video(
            id = "v6",
            title = "Stand-up Comedy: When Developers Try Talking To Real Humans",
            channelId = "c5",
            channelName = "Laugh Factory Insane",
            channelAvatar = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=120",
            subscriberCount = "1.5M subscribers",
            verified = false,
            views = "920K views",
            viewCount = 920000L,
            timeAgo = "2 weeks ago",
            duration = "08:44",
            durationSeconds = 524,
            category = "Comedy",
            description = "Live standup special: Why git merge conflicts ruin marriages, why meetings could have been emails, and how coffee became a primary food group.",
            likesCount = "91K",
            likeNumber = 91000,
            tags = listOf("Comedy", "StandUp", "Relatable", "Humor"),
            gradientStartHex = 0xFF4A154B,
            gradientEndHex = 0xFF230B24,
            accentHex = 0xFFFF007F,
            commentsCount = 1630
        ),
        Video(
            id = "v7",
            title = "Lex & Sam Altman: The Future of Quantum AI & Reasoning Models",
            channelId = "c6",
            channelName = "Insane Conversations",
            channelAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=120",
            subscriberCount = "3.7M subscribers",
            verified = true,
            views = "2.1M views",
            viewCount = 2100000L,
            timeAgo = "3 days ago",
            duration = "2:45:18",
            durationSeconds = 9918,
            category = "Podcasts",
            description = "Deep dive into artificial intelligence breakthroughs, synthetic biology, energy abundance, and humanity's trajectory in the 21st century.",
            likesCount = "130K",
            likeNumber = 130000,
            tags = listOf("Podcast", "AI", "Lex", "Future"),
            gradientStartHex = 0xFF1F1F1F,
            gradientEndHex = 0xFF0A0A0A,
            accentHex = 0xFFE0E0E0,
            commentsCount = 3890
        )
    )

    // Base YouTube Shorts
    val baseShorts = listOf(
        ShortItem(
            id = "s1",
            title = "This robot solved a Rubik's cube in 0.103 seconds! 😱",
            channelName = "Tech Insane",
            channelAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=120",
            subscriberCount = "4.82M",
            likesCount = "890K",
            likeNumber = 890000,
            commentsCount = "12.4K",
            songTitle = "Speed Demon - Insane Original Soundtrack",
            tags = listOf("#Shorts", "#Robotics", "#InsaneTech", "#Record"),
            gradientStartHex = 0xFF4B1113,
            gradientEndHex = 0xFF1A0506
        ),
        ShortItem(
            id = "s2",
            title = "POV: You forgot a single semicolon in 5,000 lines of C++ 😂",
            channelName = "DevMemes Official",
            channelAvatar = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=120",
            subscriberCount = "850K",
            likesCount = "1.2M",
            likeNumber = 1200000,
            commentsCount = "18.2K",
            songTitle = "Funny Meme Synth Drop 3",
            tags = listOf("#Shorts", "#CodingMemes", "#ProgrammerLife"),
            gradientStartHex = 0xFF142B3B,
            gradientEndHex = 0xFF07121A
        ),
        ShortItem(
            id = "s3",
            title = "Satisfying 3D Physics Simulation with 1,000,000 Golden Spheres ✨",
            channelName = "SatisfyLab",
            channelAvatar = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=120",
            subscriberCount = "1.9M",
            likesCount = "2.4M",
            likeNumber = 2400000,
            commentsCount = "24.1K",
            songTitle = "Calm Celestial Lo-Fi - Golden",
            tags = listOf("#Shorts", "#Satisfying", "#Blender3D", "#Physics"),
            gradientStartHex = 0xFF3D2C04,
            gradientEndHex = 0xFF171101
        ),
        ShortItem(
            id = "s4",
            title = "Gordon Ramsay Reacts to $500 Gold Leaf Burger 🍔",
            channelName = "Chef Supreme",
            channelAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=120",
            subscriberCount = "6.1M",
            likesCount = "750K",
            likeNumber = 750000,
            commentsCount = "9.8K",
            songTitle = "Intense Kitchen Beats",
            tags = listOf("#Shorts", "#Food", "#Ramsay", "#Burger"),
            gradientStartHex = 0xFF3B1E08,
            gradientEndHex = 0xFF170C03
        )
    )

    // Base Subscribed Channels
    val baseChannels = listOf(
        Channel(
            id = "c1",
            name = "Tech Insane",
            handle = "@techinsane",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=120",
            subscribers = "4.82M",
            videoCount = 428,
            isSubscribed = true,
            hasUnreadVideos = true
        ),
        Channel(
            id = "c2",
            name = "GamerNexus Pro",
            handle = "@gamernexuspro",
            avatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=120",
            subscribers = "2.1M",
            videoCount = 912,
            isSubscribed = true,
            hasUnreadVideos = true
        ),
        Channel(
            id = "c3",
            name = "Lofi Insane Girl",
            handle = "@lofiinsanegirl",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=120",
            subscribers = "12.4M",
            videoCount = 184,
            isSubscribed = true,
            hasUnreadVideos = false
        ),
        Channel(
            id = "c4",
            name = "Apex Explorers",
            handle = "@apexexplorers",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=120",
            subscribers = "8.9M",
            videoCount = 310,
            isSubscribed = true,
            hasUnreadVideos = true
        ),
        Channel(
            id = "c5",
            name = "Laugh Factory Insane",
            handle = "@laughfactory",
            avatarUrl = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=120",
            subscribers = "1.5M",
            videoCount = 672,
            isSubscribed = false,
            hasUnreadVideos = false
        ),
        Channel(
            id = "c6",
            name = "Insane Conversations",
            handle = "@insaneconversations",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=120",
            subscribers = "3.7M",
            videoCount = 512,
            isSubscribed = true,
            hasUnreadVideos = false
        )
    )

    // Base Playlists
    val basePlaylists = listOf(
        Playlist("p_liked", "Liked videos", 28, isPrivate = true, iconName = "thumb_up"),
        Playlist("p_watchlater", "Watch later", 14, isPrivate = true, iconName = "watch_later"),
        Playlist("p1", "Epic Coding & Dev", 36, isPrivate = false, iconName = "code"),
        Playlist("p2", "Workout & Cyber Hype Beats", 45, isPrivate = false, iconName = "music"),
        Playlist("p3", "Deep AI & Future Tech", 19, isPrivate = true, iconName = "tech")
    )

    // Initial Comments generator
    fun getInitialComments(videoId: String): List<Comment> {
        return listOf(
            Comment("cm1", videoId, "Sarah Connor", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=120", "The pacing on this video was absolute perfection! Watched from start to finish without blinking. Keep it up INSANETUBE!", "2 hours ago", 423),
            Comment("cm2", videoId, "Marcus Wright", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=120", "Timestamp 04:15 when the kernel booted on bare metal blew my mind. Incredible craftsmanship.", "5 hours ago", 189),
            Comment("cm3", videoId, "Elena Rostova", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=120", "Been waiting for this upload all week! Best creator on the entire platform 🔥", "1 day ago", 87),
            Comment("cm4", videoId, "Dave Byte", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=120", "Can you please do a deep dive into the memory management architecture next?", "2 days ago", 54)
        )
    }

    // Watch history Flow
    val watchHistory: Flow<List<WatchHistoryEntity>> = dao.getWatchHistory()

    // Saved videos Flow (Likes, Dislikes, WatchLater, Downloads)
    val savedVideos: Flow<List<SavedVideoEntity>> = dao.getAllSavedVideos()

    // Subscriptions Flow
    val subscriptions: Flow<List<SubscriptionEntity>> = dao.getSubscriptions()

    // User Uploaded videos Flow
    val userUploadedVideos: Flow<List<UserUploadedVideoEntity>> = dao.getUserUploadedVideos()

    // Removed / Filtered Fake Video IDs Flow
    val removedVideoIds: Flow<List<String>> = dao.getRemovedVideoIds()

    // Comments for video Flow (merges initial comments with Room comments)
    fun getCommentsForVideo(videoId: String): Flow<List<Comment>> {
        val initial = getInitialComments(videoId)
        return dao.getCommentsForVideo(videoId).map { dbComments ->
            val userList = dbComments.map { entity ->
                Comment(
                    id = entity.commentId,
                    videoId = entity.videoId,
                    authorName = entity.authorName,
                    authorAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=120",
                    text = entity.text,
                    timeAgo = "Just now",
                    likes = entity.likes,
                    isLiked = false,
                    isChannelOwner = false
                )
            }
            userList + initial
        }
    }

    // Actions
    suspend fun recordWatch(video: Video, progressSeconds: Int = 30) {
        dao.recordWatchHistory(
            WatchHistoryEntity(
                videoId = video.id,
                title = video.title,
                channelName = video.channelName,
                duration = video.duration,
                progressSeconds = progressSeconds,
                durationSeconds = video.durationSeconds,
                watchedTimestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun removeFromHistory(videoId: String) {
        dao.removeFromHistory(videoId)
    }

    suspend fun clearHistory() {
        dao.clearHistory()
    }

    suspend fun toggleLike(videoId: String, currentLiked: Boolean) {
        val existing = dao.getSavedVideo(videoId) ?: SavedVideoEntity(videoId = videoId)
        val newLiked = !currentLiked
        dao.upsertSavedVideo(
            existing.copy(
                isLiked = newLiked,
                isDisliked = if (newLiked) false else existing.isDisliked
            )
        )
    }

    suspend fun toggleDislike(videoId: String, currentDisliked: Boolean) {
        val existing = dao.getSavedVideo(videoId) ?: SavedVideoEntity(videoId = videoId)
        val newDisliked = !currentDisliked
        dao.upsertSavedVideo(
            existing.copy(
                isDisliked = newDisliked,
                isLiked = if (newDisliked) false else existing.isLiked
            )
        )
    }

    suspend fun toggleWatchLater(videoId: String, currentWatchLater: Boolean) {
        val existing = dao.getSavedVideo(videoId) ?: SavedVideoEntity(videoId = videoId)
        dao.upsertSavedVideo(
            existing.copy(isSavedWatchLater = !currentWatchLater)
        )
    }

    suspend fun toggleDownload(videoId: String, currentDownloaded: Boolean) {
        val existing = dao.getSavedVideo(videoId) ?: SavedVideoEntity(videoId = videoId)
        dao.upsertSavedVideo(
            existing.copy(isDownloaded = !currentDownloaded)
        )
    }

    suspend fun toggleSubscription(channelId: String, currentSubscribed: Boolean) {
        dao.upsertSubscription(
            SubscriptionEntity(
                channelId = channelId,
                isSubscribed = !currentSubscribed
            )
        )
    }

    suspend fun addComment(videoId: String, text: String, authorName: String = "You (InsaneCreator)") {
        dao.insertComment(
            UserCommentEntity(
                commentId = UUID.randomUUID().toString(),
                videoId = videoId,
                authorName = authorName,
                text = text,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun uploadVideo(
        title: String,
        description: String,
        category: String,
        duration: String,
        isShort: Boolean,
        videoUri: String? = null,
        isRealVideo: Boolean = true,
        isFake: Boolean = false,
        uploaderName: String = "Channel Member",
        uploaderHandle: String = "@member",
        soundTrack: String? = null,
        tags: String = ""
    ) {
        dao.insertUploadedVideo(
            UserUploadedVideoEntity(
                id = "uv_" + UUID.randomUUID().toString().take(8),
                title = title,
                description = description,
                category = category,
                duration = duration,
                isShort = isShort,
                videoUri = videoUri,
                isRealVideo = isRealVideo,
                isFake = isFake,
                uploaderName = uploaderName,
                uploaderHandle = uploaderHandle,
                soundTrack = soundTrack,
                tags = tags,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun removeFakeVideo(videoId: String) {
        dao.markVideoRemoved(RemovedVideoEntity(videoId = videoId))
        dao.deleteUploadedVideo(videoId)
    }

    suspend fun removeAllFakeVideos() {
        baseVideos.forEach { v ->
            dao.markVideoRemoved(RemovedVideoEntity(videoId = v.id))
        }
        baseShorts.forEach { s ->
            dao.markVideoRemoved(RemovedVideoEntity(videoId = s.id))
        }
    }

    suspend fun restoreVideo(videoId: String) {
        dao.restoreVideo(videoId)
    }

    suspend fun clearRemovedVideos() {
        dao.clearRemovedVideos()
    }

    fun getAllVideos(): List<Video> = baseVideos.map { it.copy(isFake = true, isRealVideo = false) }
    fun getAllShorts(): List<ShortItem> = baseShorts.map { it.copy(isFake = true, isReal = false) }
    fun getAllChannels(): List<Channel> = baseChannels
    fun getAllPlaylists(): List<Playlist> = basePlaylists

    // User Accounts & Authentication
    val allAccounts: Flow<List<UserAccountEntity>> = dao.getAllAccounts()
    val currentAccount: Flow<UserAccountEntity?> = dao.getCurrentAccount()

    suspend fun ensureInitialAccounts() {
        // Ensure Itz_PrinceYT always exists as top owner account with owner credentials
        val princeAccount = dao.getAccountByHandle("@Itz_PrinceYT") ?: dao.getAccountByEmail("insaneprinceyt@gmail.com")
        if (princeAccount == null) {
            dao.insertAccount(
                UserAccountEntity(
                    id = "user_itz_princeyt",
                    name = "Itz_PrinceYT",
                    handle = "@Itz_PrinceYT",
                    email = "insaneprinceyt@gmail.com",
                    password = "PRINCE_GAMING69",
                    avatarColorHex = 0xFF0284C7,
                    avatarUri = "https://images.unsplash.com/photo-1566492031773-4f4e44671857?w=240",
                    avatarPreset = "neon_gamer",
                    subscribers = "18.5M subscribers",
                    isVerified = true,
                    hasBlueTick = true,
                    isOwner = true,
                    isPremium = true,
                    videosCount = "1,420 videos",
                    totalViews = "3.4B views",
                    bio = "👑 Official Channel of Itz_PrinceYT • Gaming, Tech, Vlogs & High Octane Entertainment! Channel Owner & Verified Creator.",
                    isCurrent = true
                )
            )
        } else {
            dao.insertAccount(
                princeAccount.copy(
                    email = "insaneprinceyt@gmail.com",
                    password = "PRINCE_GAMING69",
                    isOwner = true,
                    hasBlueTick = true,
                    isVerified = true,
                    isPremium = true
                )
            )
        }

        val current = dao.getCurrentAccountDirect()
        if (current == null) {
            // Check if Kavita account exists
            val existingKavita = dao.getAccountByEmail("kavitasharma22064@gmail.com")
            if (existingKavita == null) {
                // Seed default accounts
                dao.insertAccount(
                    UserAccountEntity(
                        id = "user_kavita",
                        name = "Kavita Sharma",
                        handle = "@kavita_sharma",
                        email = "kavitasharma22064@gmail.com",
                        password = "password123",
                        avatarColorHex = 0xFFFF0033,
                        avatarUri = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=240",
                        avatarPreset = "portrait",
                        subscribers = "2.4K subscribers",
                        isVerified = true,
                        hasBlueTick = false,
                        isOwner = false,
                        isPremium = false,
                        videosCount = "48 videos",
                        totalViews = "120K views",
                        bio = "Content creator, mobile reviewer and tech enthusiast.",
                        isCurrent = false
                    )
                )
                dao.insertAccount(
                    UserAccountEntity(
                        id = "user_creator",
                        name = "Insane Creator",
                        handle = "@insane_creator",
                        email = "creator@insanetube.com",
                        password = "password123",
                        avatarColorHex = 0xFF7C3AED,
                        avatarUri = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=240",
                        avatarPreset = "cyber_creator",
                        subscribers = "1.2K subscribers",
                        isVerified = true,
                        hasBlueTick = false,
                        isOwner = false,
                        isPremium = true,
                        videosCount = "32 videos",
                        totalViews = "85K views",
                        bio = "Building the ultimate video streaming universe.",
                        isCurrent = false
                    )
                )
                dao.insertAccount(
                    UserAccountEntity(
                        id = "user_alex",
                        name = "Alex Chen",
                        handle = "@alexchen_dev",
                        email = "alex.code@techmail.com",
                        password = "password123",
                        avatarColorHex = 0xFF10B981,
                        avatarUri = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=240",
                        avatarPreset = "dev_hacker",
                        subscribers = "850 subscribers",
                        isVerified = false,
                        hasBlueTick = false,
                        isOwner = false,
                        isPremium = false,
                        videosCount = "15 videos",
                        totalViews = "35K views",
                        bio = "Coding tutorials and developer logs.",
                        isCurrent = false
                    )
                )
            }
            
            // Default active to Itz_PrinceYT
            dao.setCurrentAccount("user_itz_princeyt")
        }
    }

    suspend fun signIn(emailOrHandle: String, pass: String): Result<UserAccountEntity> {
        val trimmed = emailOrHandle.trim()
        val account = dao.getAccountByEmail(trimmed) ?: dao.getAccountByHandle(trimmed) ?: dao.getAccountByHandle("@$trimmed")
        return if (account != null) {
            val enteredPass = pass.trim()
            if (account.password.isNotBlank() && account.password == enteredPass) {
                dao.clearCurrentAccount()
                dao.setCurrentAccount(account.id)
                Result.success(account.copy(isCurrent = true))
            } else {
                Result.failure(Exception("Incorrect password. Please verify credentials."))
            }
        } else {
            Result.failure(Exception("No account found for '$trimmed'"))
        }
    }

    suspend fun signUp(name: String, handleRaw: String, email: String, pass: String): Result<UserAccountEntity> {
        val trimmedEmail = email.trim()
        val trimmedName = name.trim()
        val handle = if (handleRaw.trim().startsWith("@")) handleRaw.trim() else "@" + handleRaw.trim()

        if (trimmedEmail.isBlank() || !trimmedEmail.contains("@")) {
            return Result.failure(Exception("Please enter a valid email address"))
        }
        if (trimmedName.isBlank()) {
            return Result.failure(Exception("Please enter your name"))
        }
        if (pass.length < 4) {
            return Result.failure(Exception("Password must be at least 4 characters"))
        }

        val existingEmail = dao.getAccountByEmail(trimmedEmail)
        if (existingEmail != null) {
            return Result.failure(Exception("An account with this email already exists"))
        }
        val existingHandle = dao.getAccountByHandle(handle)
        if (existingHandle != null) {
            return Result.failure(Exception("This handle is already taken"))
        }

        val colors = listOf(0xFFFF0033, 0xFF7C3AED, 0xFF0284C7, 0xFF10B981, 0xFFF59E0B, 0xFFEC4899)
        val randomColor = colors.random()
        val newId = "user_" + UUID.randomUUID().toString().take(8)

        val newAccount = UserAccountEntity(
            id = newId,
            name = trimmedName,
            handle = handle,
            email = trimmedEmail,
            password = pass,
            avatarColorHex = randomColor,
            subscribers = "0 subscribers",
            isVerified = false,
            isCurrent = true
        )

        dao.clearCurrentAccount()
        dao.insertAccount(newAccount)
        return Result.success(newAccount)
    }

    suspend fun switchAccount(accountId: String) {
        dao.clearCurrentAccount()
        dao.setCurrentAccount(accountId)
    }

    suspend fun signOut() {
        dao.clearCurrentAccount()
    }

    suspend fun updateProfile(id: String, name: String, handleRaw: String) {
        val handle = if (handleRaw.trim().startsWith("@")) handleRaw.trim() else "@" + handleRaw.trim()
        dao.updateProfile(id, name.trim(), handle)
    }

    suspend fun updateAvatarUri(id: String, uri: String?) {
        dao.updateAvatarUri(id, uri)
    }

    suspend fun updateAvatarColor(id: String, colorHex: Long) {
        dao.updateAvatarColor(id, colorHex)
    }

    suspend fun updateAvatarPhotoAndColor(id: String, uri: String?, colorHex: Long) {
        dao.updateAvatarPhotoAndColor(id, uri, colorHex)
    }

    suspend fun togglePremium(id: String, isPremium: Boolean) {
        dao.updatePremiumStatus(id, isPremium)
    }
}
