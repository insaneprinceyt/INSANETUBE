package com.example.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.data.local.UserAccountEntity
import com.example.ui.theme.InsaneRed
import kotlinx.coroutines.delay
import java.util.UUID

private data class LiveChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: String,
    val message: String,
    val isOwner: Boolean = false,
    val avatarColorHex: Long = 0xFF0284C7
)

@Composable
fun LiveStreamScreen(
    currentAccount: UserAccountEntity?,
    onClose: () -> Unit,
    onPublishVod: (title: String, description: String, category: String, duration: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasCameraPermission = permissions[Manifest.permission.CAMERA] ?: false
        hasAudioPermission = permissions[Manifest.permission.RECORD_AUDIO] ?: false
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission || !hasAudioPermission) {
            permissionLauncher.launch(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
            )
        }
    }

    // Camera state
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_FRONT) }
    var isMicMuted by remember { mutableStateOf(false) }
    var streamStatus by remember { mutableStateOf(0) } // 0 = Setup, 1 = Live, 2 = Summary
    var streamTitle by remember { mutableStateOf(if (currentAccount?.isOwner == true) "👑 LIVE: Itz_PrinceYT Streaming & Interacting with Fans!" else "🔴 INSANETUBE Real Live Stream") }
    var streamCategory by remember { mutableStateOf("Gaming") }
    var viewerCount by remember { mutableIntStateOf(1420) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var myChatMessage by remember { mutableStateOf("") }

    // Live chat messages
    val chatMessages = remember {
        mutableStateListOf(
            LiveChatMessage(sender = "Kavita Sharma", message = "Yo Prince you are finally LIVE! 🔥", avatarColorHex = 0xFFFF0033),
            LiveChatMessage(sender = "CyberGamer99", message = "The real camera quality is insane!! 60 FPS look so clean", avatarColorHex = 0xFF0284C7),
            LiveChatMessage(sender = "Alex Chen", message = "Greetings from Seattle! Audio is crystal clear", avatarColorHex = 0xFF10B981)
        )
    }

    // Simulation loop for live stream timer and incoming chat
    LaunchedEffect(streamStatus) {
        if (streamStatus == 1) {
            val simulatedComments = listOf(
                "Drop that gameplay hype!!",
                "Hit that like button chat! 👍",
                "Can you review the new setup?",
                "Best streamer on INSANETUBE 🚀",
                "Bro this camera feed is so smooth",
                "Let's get this stream to 5K likes!",
                "Real members are in the building 🔥"
            )
            val senders = listOf("Rohan_Tech", "Sneha_Gaming", "Vikram24", "GamingBeast", "Maya_Vlogs")

            while (streamStatus == 1) {
                delay(1000)
                elapsedSeconds++
                // Fluctuate viewer count realistically
                if (elapsedSeconds % 3 == 0) {
                    val delta = (-5..15).random()
                    viewerCount = (viewerCount + delta).coerceAtLeast(100)
                }
                // Add new chat message every 4 seconds
                if (elapsedSeconds % 4 == 0) {
                    val randomSender = senders.random()
                    val randomComment = simulatedComments.random()
                    chatMessages.add(
                        LiveChatMessage(
                            sender = randomSender,
                            message = randomComment,
                            avatarColorHex = 0xFF7C3AED
                        )
                    )
                    if (chatMessages.size > 20) chatMessages.removeAt(0)
                }
            }
        }
    }

    val formatTime = remember(elapsedSeconds) {
        val mins = elapsedSeconds / 60
        val secs = elapsedSeconds % 60
        String.format("%02d:%02d", mins, secs)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("live_stream_screen")
    ) {
        // 1. Real Camera Feed in Background
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.surfaceProvider = previewView.surfaceProvider
                            }
                            val cameraSelector = CameraSelector.Builder()
                                .requireLensFacing(lensFacing)
                                .build()
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                update = { previewView ->
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(previewView.context)
                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.surfaceProvider = previewView.surfaceProvider
                            }
                            val cameraSelector = CameraSelector.Builder()
                                .requireLensFacing(lensFacing)
                                .build()
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, ContextCompat.getMainExecutor(previewView.context))
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Permission placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF1E293B), Color(0xFF0F172A))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.VideocamOff,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Camera Permission Required",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            permissionLauncher.launch(
                                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = InsaneRed)
                    ) {
                        Text("Grant Permissions", color = Color.White)
                    }
                }
            }
        }

        // Dark gradient overlay for readability of UI elements
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Black.copy(alpha = 0.6f),
                        0.25f to Color.Transparent,
                        0.6f to Color.Black.copy(alpha = 0.4f),
                        1f to Color.Black.copy(alpha = 0.9f)
                    )
                )
        )

        // 2. SETUP MODE OVERLAY
        if (streamStatus == 0) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top setup bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Switch camera button
                        IconButton(
                            onClick = {
                                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                                    CameraSelector.LENS_FACING_BACK
                                } else {
                                    CameraSelector.LENS_FACING_FRONT
                                }
                            },
                            modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(imageVector = Icons.Default.Cameraswitch, contentDescription = "Switch Camera", tint = Color.White)
                        }

                        // Mute mic button
                        IconButton(
                            onClick = { isMicMuted = !isMicMuted },
                            modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(
                                imageVector = if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                                contentDescription = "Microphone",
                                tint = if (isMicMuted) InsaneRed else Color.White
                            )
                        }
                    }
                }

                // Middle: Stream details card
                Surface(
                    color = Color.Black.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(InsaneRed)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "READY TO BROADCAST",
                                color = InsaneRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                letterSpacing = 1.sp
                            )
                        }

                        OutlinedTextField(
                            value = streamTitle,
                            onValueChange = { streamTitle = it },
                            label = { Text("Stream Title", color = Color.White.copy(alpha = 0.7f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = InsaneRed,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("stream_title_input")
                        )

                        Text(
                            text = "Category",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Gaming", "IRL Tech", "Music", "Q&A").forEach { cat ->
                                FilterChip(
                                    selected = streamCategory == cat,
                                    onClick = { streamCategory = cat },
                                    label = { Text(cat, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = InsaneRed,
                                        selectedLabelColor = Color.White,
                                        containerColor = Color.White.copy(alpha = 0.1f),
                                        labelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                // Bottom Go Live button
                Button(
                    onClick = { streamStatus = 1 },
                    colors = ButtonDefaults.buttonColors(containerColor = InsaneRed),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("start_go_live_button")
                ) {
                    Icon(imageVector = Icons.Default.Sensors, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "GO LIVE WITH REAL CAMERA",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // 3. LIVE STREAMING HUD
        if (streamStatus == 1) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Live Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // LIVE badge
                        Surface(
                            color = InsaneRed,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "LIVE",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Duration timer
                        Surface(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = formatTime,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                            )
                        }

                        // Viewers counter
                        Surface(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$viewerCount",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // End Stream Button
                    Button(
                        onClick = { streamStatus = 2 },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("End Stream", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Middle spacer
                Spacer(modifier = Modifier.weight(1f))

                // Stream Title Card
                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = streamTitle,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                                    CameraSelector.LENS_FACING_BACK
                                } else {
                                    CameraSelector.LENS_FACING_FRONT
                                }
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Cameraswitch, contentDescription = "Flip", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom: Live Chat messages stream
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(chatMessages) { msg ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(msg.avatarColorHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = msg.sender.take(1),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = msg.sender,
                                color = Color(0xFF38BDF8),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = msg.message,
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Creator Chat Input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = myChatMessage,
                        onValueChange = { myChatMessage = it },
                        placeholder = { Text("Chat as broadcaster...", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = InsaneRed,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedContainerColor = Color.Black.copy(alpha = 0.6f),
                            unfocusedContainerColor = Color.Black.copy(alpha = 0.6f)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = {
                            if (myChatMessage.isNotBlank()) {
                                chatMessages.add(
                                    LiveChatMessage(
                                        sender = currentAccount?.name ?: "Broadcaster",
                                        message = myChatMessage,
                                        isOwner = true,
                                        avatarColorHex = 0xFFFF0033
                                    )
                                )
                                myChatMessage = ""
                            }
                        }),
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            if (myChatMessage.isNotBlank()) {
                                chatMessages.add(
                                    LiveChatMessage(
                                        sender = currentAccount?.name ?: "Broadcaster",
                                        message = myChatMessage,
                                        isOwner = true,
                                        avatarColorHex = 0xFFFF0033
                                    )
                                )
                                myChatMessage = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(InsaneRed, CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                    }
                }
            }
        }

        // 4. SUMMARY / ENDED STREAM DIALOG
        if (streamStatus == 2) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(32.dp))
                        }

                        Text(
                            text = "Stream Completed!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "$viewerCount", fontSize = 18.sp, fontWeight = FontWeight.Black, color = InsaneRed)
                                Text(text = "Peak Viewers", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = formatTime, fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF38BDF8))
                                Text(text = "Stream Duration", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "+48", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF10B981))
                                Text(text = "New Subs", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Publish VOD button
                        Button(
                            onClick = {
                                onPublishVod(
                                    streamTitle,
                                    "Recorded live broadcast stream with real camera feed and chat replay. Streamed on INSANETUBE.",
                                    streamCategory,
                                    formatTime
                                )
                                onClose()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = InsaneRed),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Publish Recorded VOD to Channel", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onClose,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Done", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}
