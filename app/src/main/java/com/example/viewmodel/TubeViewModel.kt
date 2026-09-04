package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.*
import com.example.data.model.*
import com.example.data.repository.TubeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TubeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TubeRepository

    init {
        val database = TubeDatabase.getDatabase(application)
        repository = TubeRepository(database.tubeDao())
        viewModelScope.launch {
            repository.ensureInitialAccounts()
        }
    }

    // Navigation & Screen States
    private val _currentTab = MutableStateFlow(NavTab.HOME)
    val currentTab: StateFlow<NavTab> = _currentTab.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Search state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    private val _searchHistory = MutableStateFlow(
        listOf(
            "Android Jetpack Compose tutorials",
            "Build Operating System from scratch",
            "Lofi beats to relax study code",
            "Unreal Engine 5.5 photorealism",
            "Lex and Sam Altman full podcast",
            "INSANETUBE trending creators"
        )
    )
    val searchHistory: StateFlow<List<String>> = _searchHistory.asStateFlow()

    // Video Player state
    private val _activeVideo = MutableStateFlow<Video?>(null)
    val activeVideo: StateFlow<Video?> = _activeVideo.asStateFlow()

    private val _isPlayerExpanded = MutableStateFlow(false)
    val isPlayerExpanded: StateFlow<Boolean> = _isPlayerExpanded.asStateFlow()

    private val _isPlaying = MutableStateFlow(true)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPlaybackSeconds = MutableStateFlow(0)
    val currentPlaybackSeconds: StateFlow<Int> = _currentPlaybackSeconds.asStateFlow()

    private val _playbackSpeed = MutableStateFlow("1.0x")
    val playbackSpeed: StateFlow<String> = _playbackSpeed.asStateFlow()

    private val _videoQuality = MutableStateFlow("1080p")
    val videoQuality: StateFlow<String> = _videoQuality.asStateFlow()

    private val _captionsEnabled = MutableStateFlow(false)
    val captionsEnabled: StateFlow<Boolean> = _captionsEnabled.asStateFlow()

    private val _autoPlay = MutableStateFlow(true)
    val autoPlay: StateFlow<Boolean> = _autoPlay.asStateFlow()

    // Dialog & Sheet States
    private val _commentsSheetOpen = MutableStateFlow(false)
    val commentsSheetOpen: StateFlow<Boolean> = _commentsSheetOpen.asStateFlow()

    private val _createSheetOpen = MutableStateFlow(false)
    val createSheetOpen: StateFlow<Boolean> = _createSheetOpen.asStateFlow()

    private val _uploadDialogOpen = MutableStateFlow(false)
    val uploadDialogOpen: StateFlow<Boolean> = _uploadDialogOpen.asStateFlow()

    private val _uploadIsShort = MutableStateFlow(false)
    val uploadIsShort: StateFlow<Boolean> = _uploadIsShort.asStateFlow()

    private val _notificationsSheetOpen = MutableStateFlow(false)
    val notificationsSheetOpen: StateFlow<Boolean> = _notificationsSheetOpen.asStateFlow()

    private val _castDialogOpen = MutableStateFlow(false)
    val castDialogOpen: StateFlow<Boolean> = _castDialogOpen.asStateFlow()

    private val _shareSheetOpen = MutableStateFlow(false)
    val shareSheetOpen: StateFlow<Boolean> = _shareSheetOpen.asStateFlow()

    private val _playlistDialogOpen = MutableStateFlow(false)
    val playlistDialogOpen: StateFlow<Boolean> = _playlistDialogOpen.asStateFlow()

    private val _selectedVideoForMenu = MutableStateFlow<Video?>(null)
    val selectedVideoForMenu: StateFlow<Video?> = _selectedVideoForMenu.asStateFlow()

    private val _videoOptionsSheetOpen = MutableStateFlow(false)
    val videoOptionsSheetOpen: StateFlow<Boolean> = _videoOptionsSheetOpen.asStateFlow()

    // Download feedback
    private val _downloadNotification = MutableStateFlow<String?>(null)
    val downloadNotification: StateFlow<String?> = _downloadNotification.asStateFlow()

    // Incognito mode
    private val _isIncognito = MutableStateFlow(false)
    val isIncognito: StateFlow<Boolean> = _isIncognito.asStateFlow()

    // Shorts playback state
    private val _currentShortIndex = MutableStateFlow(0)
    val currentShortIndex: StateFlow<Int> = _currentShortIndex.asStateFlow()

    // Timer Job for simulated video playback
    private var playbackJob: Job? = null

    // Room Flows
    val watchHistory = repository.watchHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedVideos = repository.savedVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val subscriptions = repository.subscriptions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userUploadedVideos = repository.userUploadedVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // User Accounts & Authentication State
    val allAccounts: StateFlow<List<UserAccountEntity>> = repository.allAccounts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentAccount: StateFlow<UserAccountEntity?> = repository.currentAccount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _authSheetOpen = MutableStateFlow(false)
    val authSheetOpen: StateFlow<Boolean> = _authSheetOpen.asStateFlow()

    private val _accountSwitcherSheetOpen = MutableStateFlow(false)
    val accountSwitcherSheetOpen: StateFlow<Boolean> = _accountSwitcherSheetOpen.asStateFlow()

    private val _editProfileDialogOpen = MutableStateFlow(false)
    val editProfileDialogOpen: StateFlow<Boolean> = _editProfileDialogOpen.asStateFlow()

    private val _changeProfilePictureDialogOpen = MutableStateFlow(false)
    val changeProfilePictureDialogOpen: StateFlow<Boolean> = _changeProfilePictureDialogOpen.asStateFlow()

    // Own Channel Screen State
    private val _channelScreenOpen = MutableStateFlow(false)
    val channelScreenOpen: StateFlow<Boolean> = _channelScreenOpen.asStateFlow()

    // Creator Monetization Studio State
    private val _monetizationStudioOpen = MutableStateFlow(false)
    val monetizationStudioOpen: StateFlow<Boolean> = _monetizationStudioOpen.asStateFlow()

    // YouTube Premium Hub State
    private val _premiumHubOpen = MutableStateFlow(false)
    val premiumHubOpen: StateFlow<Boolean> = _premiumHubOpen.asStateFlow()

    // Real-time simulated estimated monthly revenue (for monetization demo)
    private val _estimatedMonthlyRevenue = MutableStateFlow(84250.80)
    val estimatedMonthlyRevenue: StateFlow<Double> = _estimatedMonthlyRevenue.asStateFlow()

    private val _authErrorMessage = MutableStateFlow<String?>(null)
    val authErrorMessage: StateFlow<String?> = _authErrorMessage.asStateFlow()

    private val _authSuccessMessage = MutableStateFlow<String?>(null)
    val authSuccessMessage: StateFlow<String?> = _authSuccessMessage.asStateFlow()

    // All videos combined (base + user uploads + saved states)
    val allVideos: StateFlow<List<Video>> = combine(
        savedVideos,
        subscriptions,
        userUploadedVideos
    ) { saved, subs, uploads ->
        val savedMap = saved.associateBy { it.videoId }
        val subsMap = subs.associateBy { it.channelId }

        val convertedUploads = uploads.map { u ->
            Video(
                id = u.id,
                title = u.title,
                channelId = "user_me",
                channelName = "You (InsaneCreator)",
                channelAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=120",
                subscriberCount = "1.2K subscribers",
                verified = false,
                views = "0 views",
                viewCount = 0L,
                timeAgo = "Just now",
                duration = u.duration,
                durationSeconds = 180,
                category = u.category,
                description = u.description,
                likesCount = "0",
                likeNumber = 0,
                isLiked = false,
                isDisliked = false,
                isSavedWatchLater = false,
                isDownloaded = false,
                isSubscribed = true,
                isShort = u.isShort,
                tags = listOf("Uploaded", u.category),
                gradientStartHex = 0xFF7C1E1E,
                gradientEndHex = 0xFF2A0A0A,
                accentHex = 0xFFFF0033,
                commentsCount = 0
            )
        }

        val allList = convertedUploads + repository.getAllVideos()
        allList.map { v ->
            val savedState = savedMap[v.id]
            val subState = subsMap[v.channelId]
            v.copy(
                isLiked = savedState?.isLiked ?: false,
                isDisliked = savedState?.isDisliked ?: false,
                isSavedWatchLater = savedState?.isSavedWatchLater ?: false,
                isDownloaded = savedState?.isDownloaded ?: false,
                isSubscribed = subState?.isSubscribed ?: v.isSubscribed
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.getAllVideos())

    // Dynamic Filtered Feed based on category
    val filteredVideos: StateFlow<List<Video>> = combine(
        allVideos,
        selectedCategory,
        searchQuery
    ) { list, cat, query ->
        var res = list
        if (cat != "All") {
            res = res.filter { it.category.equals(cat, ignoreCase = true) }
        }
        if (query.isNotBlank()) {
            res = res.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.channelName.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true) ||
                        it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
            }
        }
        res
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.getAllVideos())

    // All Shorts list
    val allShorts: StateFlow<List<ShortItem>> = MutableStateFlow(repository.getAllShorts()).asStateFlow()

    // All Channels
    val allChannels: StateFlow<List<Channel>> = combine(subscriptions) { subs ->
        val subsMap = subs.firstOrNull()?.associateBy { it.channelId } ?: emptyMap()
        repository.getAllChannels().map { ch ->
            val isSub = subsMap[ch.id]?.isSubscribed ?: ch.isSubscribed
            ch.copy(isSubscribed = isSub)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.getAllChannels())

    // Playlists
    val playlists: StateFlow<List<Playlist>> = MutableStateFlow(repository.getAllPlaylists()).asStateFlow()

    // Current video comments
    fun getCommentsForActiveVideo(): Flow<List<Comment>> {
        val id = _activeVideo.value?.id ?: return flowOf(emptyList())
        return repository.getCommentsForVideo(id)
    }

    // Navigation Tab Selection
    fun selectTab(tab: NavTab) {
        if (tab == NavTab.CREATE) {
            _createSheetOpen.value = true
        } else {
            _currentTab.value = tab
            if (tab == NavTab.SHORTS) {
                // Pause main video if watching when switching to Shorts
                _isPlaying.value = false
            }
        }
    }

    fun selectCategory(cat: String) {
        _selectedCategory.value = cat
    }

    // Video Playback Controls
    fun playVideo(video: Video) {
        _activeVideo.value = video
        _isPlayerExpanded.value = true
        _isPlaying.value = true
        _currentPlaybackSeconds.value = 0
        startPlaybackSimulation(video)

        if (!_isIncognito.value) {
            viewModelScope.launch {
                repository.recordWatch(video, progressSeconds = 15)
            }
        }
    }

    private fun startPlaybackSimulation(video: Video) {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_isPlaying.value) {
                    val next = _currentPlaybackSeconds.value + 1
                    if (next >= video.durationSeconds && video.durationSeconds > 0) {
                        if (_autoPlay.value) {
                            playNextVideo()
                        } else {
                            _isPlaying.value = false
                        }
                        break
                    } else {
                        _currentPlaybackSeconds.value = next
                    }
                }
            }
        }
    }

    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
    }

    fun seekTo(seconds: Int) {
        val total = _activeVideo.value?.durationSeconds ?: 300
        _currentPlaybackSeconds.value = seconds.coerceIn(0, total)
    }

    fun seekRelative(deltaSeconds: Int) {
        val total = _activeVideo.value?.durationSeconds ?: 300
        val current = _currentPlaybackSeconds.value
        _currentPlaybackSeconds.value = (current + deltaSeconds).coerceIn(0, total)
    }

    fun playNextVideo() {
        val current = _activeVideo.value ?: return
        val list = allVideos.value
        val currentIndex = list.indexOfFirst { it.id == current.id }
        if (currentIndex in list.indices) {
            val nextIndex = (currentIndex + 1) % list.size
            playVideo(list[nextIndex])
        }
    }

    fun playPreviousVideo() {
        val current = _activeVideo.value ?: return
        val list = allVideos.value
        val currentIndex = list.indexOfFirst { it.id == current.id }
        if (currentIndex in list.indices) {
            val prevIndex = if (currentIndex - 1 >= 0) currentIndex - 1 else list.size - 1
            playVideo(list[prevIndex])
        }
    }

    fun expandPlayer() {
        _isPlayerExpanded.value = true
    }

    fun collapsePlayer() {
        _isPlayerExpanded.value = false
    }

    fun closePlayer() {
        playbackJob?.cancel()
        _activeVideo.value = null
        _isPlayerExpanded.value = false
        _isPlaying.value = false
    }

    fun cyclePlaybackSpeed() {
        val speeds = listOf("0.5x", "0.75x", "1.0x", "1.25x", "1.5x", "2.0x")
        val currentIndex = speeds.indexOf(_playbackSpeed.value)
        val next = speeds[(currentIndex + 1) % speeds.size]
        _playbackSpeed.value = next
    }

    fun cycleQuality() {
        val qualities = listOf("Auto", "480p", "720p", "1080p", "1440p", "4K")
        val currentIndex = qualities.indexOf(_videoQuality.value)
        val next = qualities[(currentIndex + 1) % qualities.size]
        _videoQuality.value = next
    }

    fun toggleCaptions() {
        _captionsEnabled.value = !_captionsEnabled.value
    }

    fun toggleAutoPlay() {
        _autoPlay.value = !_autoPlay.value
    }

    // Like / Dislike / WatchLater / Download / Subscribe
    fun toggleLikeActiveVideo() {
        val current = _activeVideo.value ?: return
        viewModelScope.launch {
            repository.toggleLike(current.id, current.isLiked)
            _activeVideo.value = _activeVideo.value?.copy(
                isLiked = !current.isLiked,
                isDisliked = if (!current.isLiked) false else current.isDisliked
            )
        }
    }

    fun toggleDislikeActiveVideo() {
        val current = _activeVideo.value ?: return
        viewModelScope.launch {
            repository.toggleDislike(current.id, current.isDisliked)
            _activeVideo.value = _activeVideo.value?.copy(
                isDisliked = !current.isDisliked,
                isLiked = if (!current.isDisliked) false else current.isLiked
            )
        }
    }

    fun toggleWatchLater(video: Video) {
        viewModelScope.launch {
            repository.toggleWatchLater(video.id, video.isSavedWatchLater)
            if (_activeVideo.value?.id == video.id) {
                _activeVideo.value = _activeVideo.value?.copy(isSavedWatchLater = !video.isSavedWatchLater)
            }
        }
    }

    fun toggleDownload(video: Video) {
        viewModelScope.launch {
            repository.toggleDownload(video.id, video.isDownloaded)
            val isNowDownloaded = !video.isDownloaded
            _downloadNotification.value = if (isNowDownloaded) "Downloaded '${video.title.take(24)}...' to Library" else "Removed from Downloads"
            if (_activeVideo.value?.id == video.id) {
                _activeVideo.value = _activeVideo.value?.copy(isDownloaded = isNowDownloaded)
            }
            delay(3000)
            _downloadNotification.value = null
        }
    }

    fun toggleSubscribe(channelId: String, currentSubscribed: Boolean) {
        viewModelScope.launch {
            repository.toggleSubscription(channelId, currentSubscribed)
            if (_activeVideo.value?.channelId == channelId) {
                _activeVideo.value = _activeVideo.value?.copy(isSubscribed = !currentSubscribed)
            }
        }
    }

    // Comments
    fun addComment(text: String) {
        val video = _activeVideo.value ?: return
        if (text.isBlank()) return
        val current = currentAccount.value
        val author = if (current != null) current.name else "Guest User"
        viewModelScope.launch {
            repository.addComment(video.id, text.trim(), author)
        }
    }

    fun openComments() {
        _commentsSheetOpen.value = true
    }

    fun closeComments() {
        _commentsSheetOpen.value = false
    }

    // Video 3-dot options
    fun openVideoOptions(video: Video) {
        _selectedVideoForMenu.value = video
        _videoOptionsSheetOpen.value = true
    }

    fun closeVideoOptions() {
        _videoOptionsSheetOpen.value = false
        _selectedVideoForMenu.value = null
    }

    // Search
    fun openSearch() {
        _isSearchActive.value = true
    }

    fun closeSearch() {
        _isSearchActive.value = false
        _searchQuery.value = ""
    }

    fun onSearchQueryChanged(q: String) {
        _searchQuery.value = q
    }

    fun submitSearch(q: String) {
        _searchQuery.value = q
        if (q.isNotBlank() && !_searchHistory.value.contains(q)) {
            _searchHistory.value = listOf(q) + _searchHistory.value.take(7)
        }
        _isSearchActive.value = false
    }

    fun clearSearchHistoryItem(item: String) {
        _searchHistory.value = _searchHistory.value.filter { it != item }
    }

    // Upload / Create Dialog
    fun openUploadDialog(isShort: Boolean = false) {
        _createSheetOpen.value = false
        _uploadIsShort.value = isShort
        _uploadDialogOpen.value = true
    }

    fun closeUploadDialog() {
        _uploadDialogOpen.value = false
    }

    fun submitUpload(title: String, description: String, category: String, duration: String) {
        viewModelScope.launch {
            repository.uploadVideo(
                title = title,
                description = description,
                category = category,
                duration = duration,
                isShort = _uploadIsShort.value
            )
            _uploadDialogOpen.value = false
            _downloadNotification.value = "Your video '${title.take(20)}...' has been published!"
            delay(3000)
            _downloadNotification.value = null
        }
    }

    // Notifications & Cast & Share & Playlist
    fun setCreateSheetOpen(open: Boolean) { _createSheetOpen.value = open }
    fun setNotificationsSheetOpen(open: Boolean) { _notificationsSheetOpen.value = open }
    fun setCastDialogOpen(open: Boolean) { _castDialogOpen.value = open }
    fun setShareSheetOpen(open: Boolean) { _shareSheetOpen.value = open }
    fun setPlaylistDialogOpen(open: Boolean) { _playlistDialogOpen.value = open }

    fun toggleIncognito() {
        _isIncognito.value = !_isIncognito.value
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun removeFromHistory(videoId: String) {
        viewModelScope.launch {
            repository.removeFromHistory(videoId)
        }
    }

    // Shorts navigation
    fun nextShort() {
        val total = repository.getAllShorts().size
        _currentShortIndex.value = (_currentShortIndex.value + 1) % total
    }

    fun prevShort() {
        val total = repository.getAllShorts().size
        _currentShortIndex.value = if (_currentShortIndex.value - 1 >= 0) _currentShortIndex.value - 1 else total - 1
    }

    // Auth & Account Management
    fun openAuthSheet() {
        _authErrorMessage.value = null
        _authSheetOpen.value = true
    }

    fun closeAuthSheet() {
        _authSheetOpen.value = false
        _authErrorMessage.value = null
    }

    fun openAccountSwitcher() {
        _accountSwitcherSheetOpen.value = true
    }

    fun closeAccountSwitcher() {
        _accountSwitcherSheetOpen.value = false
    }

    fun openEditProfileDialog() {
        _editProfileDialogOpen.value = true
    }

    fun closeEditProfileDialog() {
        _editProfileDialogOpen.value = false
    }

    fun signIn(emailOrHandle: String, pass: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _authErrorMessage.value = null
            val result = repository.signIn(emailOrHandle, pass)
            result.onSuccess { acc ->
                _authSheetOpen.value = false
                _accountSwitcherSheetOpen.value = false
                _authSuccessMessage.value = "Signed in as ${acc.name}"
                onSuccess()
                delay(3000)
                _authSuccessMessage.value = null
            }.onFailure { err ->
                _authErrorMessage.value = err.message ?: "Sign in failed"
            }
        }
    }

    fun signUp(name: String, handle: String, email: String, pass: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _authErrorMessage.value = null
            val result = repository.signUp(name, handle, email, pass)
            result.onSuccess { acc ->
                _authSheetOpen.value = false
                _accountSwitcherSheetOpen.value = false
                _authSuccessMessage.value = "Welcome to INSANETUBE, ${acc.name}!"
                onSuccess()
                delay(3000)
                _authSuccessMessage.value = null
            }.onFailure { err ->
                _authErrorMessage.value = err.message ?: "Sign up failed"
            }
        }
    }

    fun switchAccount(accountId: String) {
        viewModelScope.launch {
            repository.switchAccount(accountId)
            _accountSwitcherSheetOpen.value = false
            val acc = allAccounts.value.find { it.id == accountId }
            _authSuccessMessage.value = "Switched to ${acc?.name ?: "account"}"
            delay(3000)
            _authSuccessMessage.value = null
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
            _accountSwitcherSheetOpen.value = false
            _authSuccessMessage.value = "Signed out. You are now browsing as Guest."
            delay(3000)
            _authSuccessMessage.value = null
        }
    }

    fun updateProfile(name: String, handle: String) {
        val current = currentAccount.value ?: return
        viewModelScope.launch {
            repository.updateProfile(current.id, name, handle)
            _editProfileDialogOpen.value = false
            _authSuccessMessage.value = "Channel profile updated!"
            delay(3000)
            _authSuccessMessage.value = null
        }
    }

    fun openChangeProfilePictureDialog() {
        _changeProfilePictureDialogOpen.value = true
    }

    fun closeChangeProfilePictureDialog() {
        _changeProfilePictureDialogOpen.value = false
    }

    fun updateProfilePicture(uri: String?) {
        val current = currentAccount.value ?: return
        viewModelScope.launch {
            repository.updateAvatarUri(current.id, uri)
            _changeProfilePictureDialogOpen.value = false
            _authSuccessMessage.value = if (uri != null) "Profile picture updated successfully!" else "Profile picture removed"
            delay(3000)
            _authSuccessMessage.value = null
        }
    }

    fun updateAvatarColor(colorHex: Long) {
        val current = currentAccount.value ?: return
        viewModelScope.launch {
            repository.updateAvatarColor(current.id, colorHex)
            _changeProfilePictureDialogOpen.value = false
            _authSuccessMessage.value = "Avatar theme color updated!"
            delay(3000)
            _authSuccessMessage.value = null
        }
    }

    fun setAvatarPreset(presetUrl: String, colorHex: Long) {
        val current = currentAccount.value ?: return
        viewModelScope.launch {
            repository.updateAvatarPhotoAndColor(current.id, presetUrl, colorHex)
            _changeProfilePictureDialogOpen.value = false
            _authSuccessMessage.value = "Profile picture preset applied!"
            delay(3000)
            _authSuccessMessage.value = null
        }
    }

    fun clearAuthError() {
        _authErrorMessage.value = null
    }

    fun openChannelScreen() {
        _channelScreenOpen.value = true
    }

    fun closeChannelScreen() {
        _channelScreenOpen.value = false
    }

    fun openMonetizationStudio() {
        _monetizationStudioOpen.value = true
    }

    fun closeMonetizationStudio() {
        _monetizationStudioOpen.value = false
    }

    fun openPremiumHub() {
        _premiumHubOpen.value = true
    }

    fun closePremiumHub() {
        _premiumHubOpen.value = false
    }

    fun togglePremiumSubscription() {
        val current = currentAccount.value ?: return
        val newStatus = !current.isPremium
        viewModelScope.launch {
            repository.togglePremium(current.id, newStatus)
            _authSuccessMessage.value = if (newStatus) {
                "🌟 YouTube Premium Activated! Enjoy ad-free & background play."
            } else {
                "Premium subscription cancelled."
            }
            delay(3500)
            _authSuccessMessage.value = null
        }
    }

    fun simulateSuperThanks(amount: Double) {
        _estimatedMonthlyRevenue.value += amount
        _authSuccessMessage.value = "🎉 Super Thanks of $${String.format("%.2f", amount)} received! Added to Monetization Balance."
        viewModelScope.launch {
            delay(3500)
            _authSuccessMessage.value = null
        }
    }
}
