package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserAccountEntity
import com.example.ui.theme.InsaneRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOptionsSheet(
    currentAccount: UserAccountEntity?,
    onDismiss: () -> Unit,
    onCreateShortClick: () -> Unit,
    onUploadVideoClick: () -> Unit,
    onGoLiveClick: () -> Unit,
    onReviewVideosClick: () -> Unit,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMemberRequiredAlert by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.testTag("create_options_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Create & Upload",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (currentAccount != null) "Logged in as ${currentAccount.name}" else "Member sign in required to upload",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (currentAccount != null) Color(0xFF10B981) else Color(0xFFF87171)
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Non-member banner
            if (currentAccount == null) {
                Surface(
                    color = InsaneRed.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, InsaneRed.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = InsaneRed, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Only real members can upload videos and go live. Sign in with your email & password.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = {
                            onDismiss()
                            onSignInClick()
                        }) {
                            Text("Sign In", color = InsaneRed, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            // 1. Upload Real Video (Device Gallery Picker)
            CreateOptionItem(
                icon = Icons.Default.FileUpload,
                title = "Upload real video",
                subtitle = "Select real video file from your phone storage",
                onClick = {
                    if (currentAccount != null) {
                        onUploadVideoClick()
                    } else {
                        showMemberRequiredAlert = true
                    }
                },
                badgeText = "REAL",
                badgeColor = Color(0xFF10B981),
                testTag = "upload_video_option"
            )

            // 2. Make Short (Real or Fake/Demo)
            CreateOptionItem(
                icon = Icons.Default.Bolt,
                title = "Create a Short (Real & Demo)",
                subtitle = "Pick real short video from device or design simulated reel",
                onClick = {
                    if (currentAccount != null) {
                        onCreateShortClick()
                    } else {
                        showMemberRequiredAlert = true
                    }
                },
                badgeText = "REAL / DEMO",
                badgeColor = Color(0xFF38BDF8),
                testTag = "create_short_option"
            )

            // 3. Go Live (Real Camera Feed)
            CreateOptionItem(
                icon = Icons.Default.Sensors,
                title = "Go live (Real Camera)",
                subtitle = "Broadcast real-time video with front/back camera & live chat",
                onClick = {
                    if (currentAccount != null) {
                        onGoLiveClick()
                    } else {
                        showMemberRequiredAlert = true
                    }
                },
                badgeText = "REAL LIVE",
                badgeColor = InsaneRed,
                testTag = "go_live_option"
            )

            // 4. Review & Moderate Fake Videos
            CreateOptionItem(
                icon = Icons.Default.Shield,
                title = "Review & remove fake videos",
                subtitle = "Manage demo videos, review flags, or purge fake content",
                onClick = {
                    if (currentAccount != null) {
                        onReviewVideosClick()
                    } else {
                        showMemberRequiredAlert = true
                    }
                },
                badgeText = "MODERATION",
                badgeColor = Color(0xFFF59E0B),
                testTag = "review_videos_option"
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showMemberRequiredAlert) {
        AlertDialog(
            onDismissRequest = { showMemberRequiredAlert = false },
            title = { Text("Real Member Required", fontWeight = FontWeight.Bold) },
            text = {
                Text("Only real registered members can upload videos, create shorts, go live, and review fake videos. Please sign in or register an account.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showMemberRequiredAlert = false
                        onDismiss()
                        onSignInClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = InsaneRed)
                ) {
                    Text("Sign In to Account", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showMemberRequiredAlert = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun CreateOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    badgeText: String? = null,
    badgeColor: Color = InsaneRed,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = InsaneRed,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (badgeText != null) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        color = badgeColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = badgeText,
                            color = badgeColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

/**
 * Real Video Upload Dialog allowing user to pick video from device storage via Android Photo/Video Picker!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealVideoUploadDialog(
    currentAccount: UserAccountEntity?,
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        description: String,
        category: String,
        duration: String,
        videoUri: String?,
        isRealVideo: Boolean
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Tech") }
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedVideoName by remember { mutableStateOf<String?>(null) }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedVideoUri = uri
            selectedVideoName = uri.lastPathSegment ?: "Device_Video.mp4"
            if (title.isBlank()) {
                val clean = selectedVideoName?.substringBeforeLast(".")?.replace("_", " ") ?: ""
                title = if (clean.isNotBlank()) clean else "My Real Video"
            }
        }
    }

    val categories = listOf("Tech", "Gaming", "Music", "Comedy", "Podcasts", "Trending")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.FileUpload, contentDescription = null, tint = InsaneRed)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Upload Real Video",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Video File Picker Section
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.dp,
                        if (selectedVideoUri != null) Color(0xFF10B981) else InsaneRed.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (selectedVideoUri != null) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = "Real Video Selected",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF10B981)
                            )
                            Text(
                                text = selectedVideoName ?: selectedVideoUri.toString(),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                            OutlinedButton(
                                onClick = {
                                    videoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                    )
                                },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text("Change Video File", fontSize = 11.sp)
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.VideoFile,
                                contentDescription = null,
                                tint = InsaneRed,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = "Select Real Video From Phone Storage",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Picks actual MP4 or video files from your device gallery",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Button(
                                onClick = {
                                    videoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = InsaneRed),
                                modifier = Modifier.testTag("pick_device_video_button")
                            ) {
                                Icon(imageVector = Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Browse Device Storage", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Video Title *") },
                    placeholder = { Text("Enter video title") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("upload_title_input")
                )

                // Description Input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    placeholder = { Text("Tell viewers about your video (#tags)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .testTag("upload_description_input")
                )

                // Category selector
                Text(
                    text = "Category:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.take(3).forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = InsaneRed,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val duration = if (selectedVideoUri != null) "08:30" else "12:15"
                        onSubmit(
                            title,
                            description,
                            selectedCategory,
                            duration,
                            selectedVideoUri?.toString(),
                            true // isRealVideo
                        )
                    }
                },
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = InsaneRed),
                modifier = Modifier.testTag("upload_confirm_button")
            ) {
                Text("Publish Real Video", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        modifier = modifier
    )
}

/**
 * Short Maker Dialog supporting BOTH "Real Short" (from gallery/recorded) and "Fake / Demo Short" (creative templates)!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortMakerDialog(
    currentAccount: UserAccountEntity?,
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        soundTrack: String,
        tags: String,
        videoUri: String?,
        isReal: Boolean,
        isFake: Boolean
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    var shortMode by remember { mutableStateOf("Real") } // "Real" or "Fake"
    var title by remember { mutableStateOf("") }
    var soundTrack by remember { mutableStateOf("Original Sound") }
    var tags by remember { mutableStateOf("#Shorts #Viral #INSANETUBE") }
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedVideoName by remember { mutableStateOf<String?>(null) }
    var fakeTemplateStyle by remember { mutableStateOf("Gaming Clutch") }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedVideoUri = uri
            selectedVideoName = uri.lastPathSegment ?: "Short_Reel.mp4"
            if (title.isBlank()) {
                title = "Epic Reel - " + (selectedVideoName?.substringBeforeLast(".") ?: "Short")
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        title = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = InsaneRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Create Short Reel",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Switch between Real Short and Fake/Demo Short
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = shortMode == "Real",
                        onClick = { shortMode = "Real" },
                        label = { Text("📱 Real Short", fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = InsaneRed,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = shortMode == "Fake",
                        onClick = { shortMode = "Fake" },
                        label = { Text("✨ Fake / Demo Short", fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF0284C7),
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (shortMode == "Real") {
                    // REAL SHORT PICKER
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            1.dp,
                            if (selectedVideoUri != null) Color(0xFF10B981) else InsaneRed.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (selectedVideoUri != null) {
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(32.dp))
                                Text("Real Short Video Attached", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF10B981))
                                Text(selectedVideoName ?: selectedVideoUri.toString(), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                                OutlinedButton(
                                    onClick = {
                                        videoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
                                    }
                                ) {
                                    Text("Change Short File", fontSize = 11.sp)
                                }
                            } else {
                                Icon(imageVector = Icons.Default.PhoneAndroid, contentDescription = null, tint = InsaneRed, modifier = Modifier.size(32.dp))
                                Text("Select Vertical Video from Storage", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground)
                                Button(
                                    onClick = {
                                        videoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = InsaneRed)
                                ) {
                                    Text("Pick Reel Video", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    // FAKE / DEMO SHORT TEMPLATE SELECTOR
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("Choose Simulated Short Style:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                            val styles = listOf("Gaming Clutch", "Cyber Neon Loop", "Lo-Fi Chill Beat", "Tech Unboxing")
                            styles.forEach { style ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            fakeTemplateStyle = style
                                            if (title.isBlank() || styles.any { title.contains(it) }) {
                                                title = "INSANE $style Moment 🔥"
                                            }
                                        }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = fakeTemplateStyle == style,
                                        onClick = { fakeTemplateStyle = style }
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(style, fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground)
                                }
                            }
                        }
                    }
                }

                // Short Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Short Caption / Title *") },
                    placeholder = { Text("Enter a catchy caption") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("short_title_input")
                )

                // Sound Track
                OutlinedTextField(
                    value = soundTrack,
                    onValueChange = { soundTrack = it },
                    label = { Text("Soundtrack / Audio") },
                    placeholder = { Text("e.g. Trending Beat #1, Original Sound") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Hashtags
                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Hashtags") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val isReal = shortMode == "Real"
                        onSubmit(
                            title,
                            soundTrack,
                            tags,
                            if (isReal) selectedVideoUri?.toString() else null,
                            isReal,
                            !isReal // isFake
                        )
                    }
                },
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (shortMode == "Real") InsaneRed else Color(0xFF0284C7)
                ),
                modifier = Modifier.testTag("short_confirm_button")
            ) {
                Text(
                    text = if (shortMode == "Real") "Publish Real Short" else "Publish Demo Short",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        modifier = modifier
    )
}
