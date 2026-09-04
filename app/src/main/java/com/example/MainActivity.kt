package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.NavTab
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.InsaneRed
import com.example.ui.theme.InsaneTubeTheme
import com.example.viewmodel.TubeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InsaneTubeTheme(darkTheme = true) {
                InsaneTubeApp()
            }
        }
    }
}

@Composable
fun InsaneTubeApp(
    viewModel: TubeViewModel = viewModel()
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    // State Collection
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val filteredVideos by viewModel.filteredVideos.collectAsStateWithLifecycle()
    val allVideos by viewModel.allVideos.collectAsStateWithLifecycle()
    val allShorts by viewModel.allShorts.collectAsStateWithLifecycle()
    val allChannels by viewModel.allChannels.collectAsStateWithLifecycle()
    val playlists by viewModel.playlists.collectAsStateWithLifecycle()
    val watchHistory by viewModel.watchHistory.collectAsStateWithLifecycle()

    // Player State
    val activeVideo by viewModel.activeVideo.collectAsStateWithLifecycle()
    val isPlayerExpanded by viewModel.isPlayerExpanded.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val currentPlaybackSeconds by viewModel.currentPlaybackSeconds.collectAsStateWithLifecycle()
    val playbackSpeed by viewModel.playbackSpeed.collectAsStateWithLifecycle()
    val videoQuality by viewModel.videoQuality.collectAsStateWithLifecycle()
    val captionsEnabled by viewModel.captionsEnabled.collectAsStateWithLifecycle()
    val autoPlay by viewModel.autoPlay.collectAsStateWithLifecycle()

    // Dialog & Sheet States
    val commentsSheetOpen by viewModel.commentsSheetOpen.collectAsStateWithLifecycle()
    val createSheetOpen by viewModel.createSheetOpen.collectAsStateWithLifecycle()
    val uploadDialogOpen by viewModel.uploadDialogOpen.collectAsStateWithLifecycle()
    val uploadIsShort by viewModel.uploadIsShort.collectAsStateWithLifecycle()
    val notificationsSheetOpen by viewModel.notificationsSheetOpen.collectAsStateWithLifecycle()
    val castDialogOpen by viewModel.castDialogOpen.collectAsStateWithLifecycle()
    val shareSheetOpen by viewModel.shareSheetOpen.collectAsStateWithLifecycle()
    val videoOptionsSheetOpen by viewModel.videoOptionsSheetOpen.collectAsStateWithLifecycle()
    val selectedVideoForMenu by viewModel.selectedVideoForMenu.collectAsStateWithLifecycle()
    val downloadNotification by viewModel.downloadNotification.collectAsStateWithLifecycle()
    val isIncognito by viewModel.isIncognito.collectAsStateWithLifecycle()
    val currentShortIndex by viewModel.currentShortIndex.collectAsStateWithLifecycle()

    // Real Upload, Short Maker, Live Stream, and Moderation states
    val realUploadOpen by viewModel.realUploadOpen.collectAsStateWithLifecycle()
    val shortMakerOpen by viewModel.shortMakerOpen.collectAsStateWithLifecycle()
    val liveStreamOpen by viewModel.liveStreamOpen.collectAsStateWithLifecycle()
    val reviewVideosOpen by viewModel.reviewVideosOpen.collectAsStateWithLifecycle()

    // Auth & Accounts State
    val currentAccount by viewModel.currentAccount.collectAsStateWithLifecycle()
    val allAccounts by viewModel.allAccounts.collectAsStateWithLifecycle()
    val authSheetOpen by viewModel.authSheetOpen.collectAsStateWithLifecycle()
    val accountSwitcherSheetOpen by viewModel.accountSwitcherSheetOpen.collectAsStateWithLifecycle()
    val editProfileDialogOpen by viewModel.editProfileDialogOpen.collectAsStateWithLifecycle()
    val changeProfilePictureDialogOpen by viewModel.changeProfilePictureDialogOpen.collectAsStateWithLifecycle()
    val authErrorMessage by viewModel.authErrorMessage.collectAsStateWithLifecycle()
    val authSuccessMessage by viewModel.authSuccessMessage.collectAsStateWithLifecycle()

    // Channel, Monetization & Premium Feature States
    val channelScreenOpen by viewModel.channelScreenOpen.collectAsStateWithLifecycle()
    val monetizationStudioOpen by viewModel.monetizationStudioOpen.collectAsStateWithLifecycle()
    val premiumHubOpen by viewModel.premiumHubOpen.collectAsStateWithLifecycle()
    val estimatedMonthlyRevenue by viewModel.estimatedMonthlyRevenue.collectAsStateWithLifecycle()

    // Search State
    val isSearchActive by viewModel.isSearchActive.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchHistory by viewModel.searchHistory.collectAsStateWithLifecycle()

    // Comments for active video
    val activeVideoComments by viewModel.getCommentsForActiveVideo()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    val categories = listOf("All", "Gaming", "Music", "Tech", "Podcasts", "Comedy", "Trending", "Live")

    // Handle Back Press
    BackHandler(enabled = liveStreamOpen || reviewVideosOpen || channelScreenOpen || isPlayerExpanded || isSearchActive || currentTab != NavTab.HOME) {
        when {
            liveStreamOpen -> viewModel.setLiveStreamOpen(false)
            reviewVideosOpen -> viewModel.setReviewVideosOpen(false)
            channelScreenOpen -> viewModel.closeChannelScreen()
            isSearchActive -> viewModel.closeSearch()
            isPlayerExpanded -> viewModel.collapsePlayer()
            currentTab != NavTab.HOME -> viewModel.selectTab(NavTab.HOME)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("insanetube_main_app")
    ) {
        if (isSearchActive) {
            // Full Search Screen Overlay
            SearchScreen(
                searchQuery = searchQuery,
                searchHistory = searchHistory,
                matchingVideos = filteredVideos,
                onQueryChange = { viewModel.onSearchQueryChanged(it) },
                onSubmitSearch = { viewModel.submitSearch(it) },
                onClearHistoryItem = { viewModel.clearSearchHistoryItem(it) },
                onCloseSearch = { viewModel.closeSearch() },
                onVideoClick = { video ->
                    viewModel.closeSearch()
                    viewModel.playVideo(video)
                }
            )
        } else {
            Scaffold(
                topBar = {
                    if (!isPlayerExpanded && currentTab != NavTab.SHORTS) {
                        TubeTopBar(
                            onSearchClick = { viewModel.openSearch() },
                            onNotificationsClick = { viewModel.setNotificationsSheetOpen(true) },
                            onCastClick = { viewModel.setCastDialogOpen(true) },
                            onProfileClick = {
                                if (currentAccount != null) {
                                    viewModel.openAccountSwitcher()
                                } else {
                                    viewModel.openAuthSheet()
                                }
                            },
                            currentAccount = currentAccount,
                            isIncognito = isIncognito
                        )
                    }
                },
                bottomBar = {
                    if (!isPlayerExpanded) {
                        Column {
                            // Mini Player docked directly above bottom bar
                            if (activeVideo != null) {
                                VideoPlayerSheet(
                                    video = activeVideo,
                                    isExpanded = false,
                                    isPlaying = isPlaying,
                                    currentSeconds = currentPlaybackSeconds,
                                    playbackSpeed = playbackSpeed,
                                    videoQuality = videoQuality,
                                    captionsEnabled = captionsEnabled,
                                    autoPlay = autoPlay,
                                    relatedVideos = allVideos,
                                    onTogglePlay = { viewModel.togglePlayPause() },
                                    onSeekTo = { viewModel.seekTo(it) },
                                    onSeekRelative = { viewModel.seekRelative(it) },
                                    onPlayNext = { viewModel.playNextVideo() },
                                    onPlayPrev = { viewModel.playPreviousVideo() },
                                    onExpand = { viewModel.expandPlayer() },
                                    onCollapse = { viewModel.collapsePlayer() },
                                    onClose = { viewModel.closePlayer() },
                                    onToggleLike = { viewModel.toggleLikeActiveVideo() },
                                    onToggleDislike = { viewModel.toggleDislikeActiveVideo() },
                                    onToggleWatchLater = { viewModel.toggleWatchLater(it) },
                                    onToggleDownload = { viewModel.toggleDownload(it) },
                                    onToggleSubscribe = { id, sub -> viewModel.toggleSubscribe(id, sub) },
                                    onOpenComments = { viewModel.openComments() },
                                    onShareClick = { viewModel.setShareSheetOpen(true) },
                                    onCycleSpeed = { viewModel.cyclePlaybackSpeed() },
                                    onCycleQuality = { viewModel.cycleQuality() },
                                    onToggleCaptions = { viewModel.toggleCaptions() },
                                    onToggleAutoPlay = { viewModel.toggleAutoPlay() },
                                    onVideoClick = { viewModel.playVideo(it) }
                                )
                            }

                            TubeBottomNavBar(
                                currentTab = currentTab,
                                onTabSelected = { viewModel.selectTab(it) }
                            )
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentTab) {
                        NavTab.HOME -> {
                            HomeScreen(
                                videos = filteredVideos,
                                shorts = allShorts,
                                categories = categories,
                                selectedCategory = selectedCategory,
                                onCategorySelected = { viewModel.selectCategory(it) },
                                onVideoClick = { viewModel.playVideo(it) },
                                onVideoMoreClick = { viewModel.openVideoOptions(it) },
                                onShortClick = {
                                    viewModel.selectTab(NavTab.SHORTS)
                                }
                            )
                        }

                        NavTab.SHORTS -> {
                            ShortsPlayerScreen(
                                shorts = allShorts,
                                currentIndex = currentShortIndex,
                                onNextShort = { viewModel.nextShort() },
                                onPrevShort = { viewModel.prevShort() },
                                onOpenComments = { viewModel.openComments() },
                                onShareClick = { viewModel.setShareSheetOpen(true) }
                            )
                        }

                        NavTab.CREATE -> {
                            // Handled by bottom sheet modal
                        }

                        NavTab.SUBSCRIPTIONS -> {
                            SubscriptionsScreen(
                                channels = allChannels,
                                videos = allVideos,
                                onVideoClick = { viewModel.playVideo(it) },
                                onVideoMoreClick = { viewModel.openVideoOptions(it) },
                                onChannelClick = { channel ->
                                    viewModel.selectCategory("All")
                                }
                            )
                        }

                        NavTab.YOU -> {
                            LibraryScreen(
                                currentAccount = currentAccount,
                                watchHistory = watchHistory,
                                savedVideos = allVideos,
                                playlists = playlists,
                                isIncognito = isIncognito,
                                onToggleIncognito = { viewModel.toggleIncognito() },
                                onClearHistory = { viewModel.clearHistory() },
                                onVideoClick = { viewModel.playVideo(it) },
                                onSignInClick = { viewModel.openAuthSheet() },
                                onSwitchAccountClick = { viewModel.openAccountSwitcher() },
                                onEditProfileClick = { viewModel.openEditProfileDialog() },
                                onSignOutClick = { viewModel.signOut() },
                                onViewChannelClick = { viewModel.openChannelScreen() },
                                onMonetizationClick = { viewModel.openMonetizationStudio() },
                                onPremiumClick = { viewModel.openPremiumHub() },
                                onChangeProfilePictureClick = { viewModel.openChangeProfilePictureDialog() },
                                estimatedRevenue = estimatedMonthlyRevenue
                            )
                        }
                    }
                }
            }
        }

        // Full Screen Video Player Overlay
        if (isPlayerExpanded && activeVideo != null) {
            VideoPlayerSheet(
                video = activeVideo,
                isExpanded = true,
                isPlaying = isPlaying,
                currentSeconds = currentPlaybackSeconds,
                playbackSpeed = playbackSpeed,
                videoQuality = videoQuality,
                captionsEnabled = captionsEnabled,
                autoPlay = autoPlay,
                relatedVideos = allVideos,
                onTogglePlay = { viewModel.togglePlayPause() },
                onSeekTo = { viewModel.seekTo(it) },
                onSeekRelative = { viewModel.seekRelative(it) },
                onPlayNext = { viewModel.playNextVideo() },
                onPlayPrev = { viewModel.playPreviousVideo() },
                onExpand = { viewModel.expandPlayer() },
                onCollapse = { viewModel.collapsePlayer() },
                onClose = { viewModel.closePlayer() },
                onToggleLike = { viewModel.toggleLikeActiveVideo() },
                onToggleDislike = { viewModel.toggleDislikeActiveVideo() },
                onToggleWatchLater = { viewModel.toggleWatchLater(it) },
                onToggleDownload = { viewModel.toggleDownload(it) },
                onToggleSubscribe = { id, sub -> viewModel.toggleSubscribe(id, sub) },
                onOpenComments = { viewModel.openComments() },
                onShareClick = { viewModel.setShareSheetOpen(true) },
                onCycleSpeed = { viewModel.cyclePlaybackSpeed() },
                onCycleQuality = { viewModel.cycleQuality() },
                onToggleCaptions = { viewModel.toggleCaptions() },
                onToggleAutoPlay = { viewModel.toggleAutoPlay() },
                onVideoClick = { viewModel.playVideo(it) }
            )
        }

        // In-App Notification / Download Snack
        downloadNotification?.let { msg ->
            Surface(
                color = Color(0xFF1E293B),
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 76.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = msg,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Comments Bottom Sheet
        if (commentsSheetOpen) {
            CommentsBottomSheet(
                comments = activeVideoComments,
                onClose = { viewModel.closeComments() },
                onAddComment = { text -> viewModel.addComment(text) }
            )
        }

        // Create (+) Action Bottom Sheet
        if (createSheetOpen) {
            CreateOptionsSheet(
                currentAccount = currentAccount,
                onDismiss = { viewModel.setCreateSheetOpen(false) },
                onCreateShortClick = { viewModel.setShortMakerOpen(true) },
                onUploadVideoClick = { viewModel.setRealUploadOpen(true) },
                onGoLiveClick = { viewModel.setLiveStreamOpen(true) },
                onReviewVideosClick = { viewModel.setReviewVideosOpen(true) },
                onSignInClick = { viewModel.openAuthSheet() }
            )
        }

        // Real Video Upload Dialog (picks real device videos)
        if (realUploadOpen) {
            RealVideoUploadDialog(
                currentAccount = currentAccount,
                onDismiss = { viewModel.setRealUploadOpen(false) },
                onSubmit = { title, desc, cat, dur, uri, isReal ->
                    viewModel.submitRealUpload(title, desc, cat, dur, uri, isReal)
                }
            )
        }

        // Real & Fake Short Reel Maker Dialog
        if (shortMakerOpen) {
            ShortMakerDialog(
                currentAccount = currentAccount,
                onDismiss = { viewModel.setShortMakerOpen(false) },
                onSubmit = { title, soundTrack, tags, uri, isReal, isFake ->
                    viewModel.submitShort(title, soundTrack, tags, uri, isReal, isFake)
                }
            )
        }

        // Real Camera Live Streaming Screen
        if (liveStreamOpen) {
            LiveStreamScreen(
                currentAccount = currentAccount,
                onClose = { viewModel.setLiveStreamOpen(false) },
                onPublishVod = { title, desc, cat, dur ->
                    viewModel.publishLiveStreamVod(title, desc, cat, dur)
                }
            )
        }

        // Review & Moderate Fake Videos Sheet (Members Only)
        if (reviewVideosOpen) {
            VideoReviewSheet(
                allVideos = allVideos,
                currentAccount = currentAccount,
                onDismiss = { viewModel.setReviewVideosOpen(false) },
                onRemoveVideo = { videoId ->
                    viewModel.removeFakeVideo(videoId)
                },
                onRemoveAllFakeVideos = {
                    viewModel.removeAllFakeVideos()
                },
                onSignInClick = {
                    viewModel.setReviewVideosOpen(false)
                    viewModel.openAuthSheet()
                }
            )
        }

        // Notifications Sheet
        if (notificationsSheetOpen) {
            NotificationsSheet(
                onClose = { viewModel.setNotificationsSheetOpen(false) }
            )
        }

        // Cast Dialog
        if (castDialogOpen) {
            CastDialog(
                onDismiss = { viewModel.setCastDialogOpen(false) }
            )
        }

        // Share Sheet
        if (shareSheetOpen) {
            ShareSheet(
                onDismiss = { viewModel.setShareSheetOpen(false) },
                onCopyLink = {
                    clipboardManager.setText(AnnotatedString("https://insanetube.app/watch?v=${activeVideo?.id ?: "v1"}"))
                    Toast.makeText(context, "Link copied to clipboard!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Video Options 3-dot Bottom Sheet
        if (videoOptionsSheetOpen) {
            VideoOptionsBottomSheet(
                video = selectedVideoForMenu,
                isMember = currentAccount != null,
                onDismiss = { viewModel.closeVideoOptions() },
                onToggleWatchLater = { viewModel.toggleWatchLater(it) },
                onToggleDownload = { viewModel.toggleDownload(it) },
                onShareClick = { viewModel.setShareSheetOpen(true) },
                onRemoveFakeVideo = { videoId ->
                    viewModel.removeFakeVideo(videoId)
                }
            )
        }

        // Authentication Bottom Sheet (Sign In & Sign Up)
        AuthBottomSheet(
            isOpen = authSheetOpen,
            errorMessage = authErrorMessage,
            onDismiss = { viewModel.closeAuthSheet() },
            onSignIn = { email, pass -> viewModel.signIn(email, pass) },
            onSignUp = { name, handle, email, pass -> viewModel.signUp(name, handle, email, pass) },
            onClearError = { viewModel.clearAuthError() }
        )

        // Account Switcher Bottom Sheet
        AccountSwitcherSheet(
            isOpen = accountSwitcherSheetOpen,
            accounts = allAccounts,
            currentAccount = currentAccount,
            isIncognito = isIncognito,
            onDismiss = { viewModel.closeAccountSwitcher() },
            onSelectAccount = { viewModel.switchAccount(it) },
            onAddAccountClick = {
                viewModel.closeAccountSwitcher()
                viewModel.openAuthSheet()
            },
            onToggleIncognito = {
                viewModel.closeAccountSwitcher()
                viewModel.toggleIncognito()
            },
            onSignOutClick = { viewModel.signOut() },
            onEditProfileClick = {
                viewModel.closeAccountSwitcher()
                viewModel.openEditProfileDialog()
            },
            onViewChannelClick = { viewModel.openChannelScreen() },
            onMonetizationClick = { viewModel.openMonetizationStudio() },
            onPremiumClick = { viewModel.openPremiumHub() },
            onChangeProfilePictureClick = { viewModel.openChangeProfilePictureDialog() }
        )

        // Edit Profile Channel Dialog
        EditProfileDialog(
            isOpen = editProfileDialogOpen,
            currentAccount = currentAccount,
            onDismiss = { viewModel.closeEditProfileDialog() },
            onSave = { name, handle -> viewModel.updateProfile(name, handle) },
            onChangeProfilePictureClick = { viewModel.openChangeProfilePictureDialog() }
        )

        // Change Profile Picture Dialog
        ChangeProfilePictureDialog(
            isOpen = changeProfilePictureDialogOpen,
            account = currentAccount,
            onDismiss = { viewModel.closeChangeProfilePictureDialog() },
            onSaveAvatarUri = { uri -> viewModel.updateProfilePicture(uri) },
            onSaveAvatarColor = { colorHex -> viewModel.updateAvatarColor(colorHex) },
            onSavePreset = { presetUrl, colorHex -> viewModel.setAvatarPreset(presetUrl, colorHex) }
        )

        // YouTube Premium Hub Bottom Sheet
        if (premiumHubOpen) {
            PremiumHubSheet(
                account = currentAccount,
                onTogglePremium = { viewModel.togglePremiumSubscription() },
                onDismiss = { viewModel.closePremiumHub() }
            )
        }

        val activeUser = currentAccount

        // Creator Monetization Studio Bottom Sheet
        if (monetizationStudioOpen && activeUser != null) {
            MonetizationStudioSheet(
                account = activeUser,
                estimatedRevenue = estimatedMonthlyRevenue,
                onSimulateTip = { viewModel.simulateSuperThanks(it) },
                onDismiss = { viewModel.closeMonetizationStudio() }
            )
        }

        // Full Screen Channel Profile Screen (See own channel, subscriber count, blue tick, videos, stats)
        if (channelScreenOpen && activeUser != null) {
            ChannelProfileScreen(
                channelAccount = activeUser,
                channelVideos = allVideos.filter { it.channelId == activeUser.id || it.channelName == activeUser.name },
                onBackClick = { viewModel.closeChannelScreen() },
                onVideoClick = { video ->
                    viewModel.closeChannelScreen()
                    viewModel.playVideo(video)
                },
                onOpenCustomize = {
                    viewModel.closeChannelScreen()
                    viewModel.openEditProfileDialog()
                },
                onOpenMonetization = {
                    viewModel.openMonetizationStudio()
                },
                onOpenPremium = {
                    viewModel.openPremiumHub()
                },
                onOpenChangeProfilePicture = {
                    viewModel.openChangeProfilePictureDialog()
                }
            )
        }

        // Auth Feedback Snackbar Banner
        authSuccessMessage?.let { msg ->
            Surface(
                color = Color(0xFF166534),
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 76.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = msg,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
