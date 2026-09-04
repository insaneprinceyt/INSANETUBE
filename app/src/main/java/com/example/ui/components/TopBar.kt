package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserAccountEntity
import com.example.ui.theme.InsaneRed

@Composable
fun TubeTopBar(
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onCastClick: () -> Unit,
    onProfileClick: () -> Unit,
    currentAccount: UserAccountEntity? = null,
    isIncognito: Boolean = false,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // INSANETUBE Logo & Branding
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { /* Reset or scroll to top */ }
                    .padding(end = 8.dp)
                    .testTag("app_logo")
            ) {
                // Play Icon Badge
                Box(
                    modifier = Modifier
                        .size(32.dp, 24.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(InsaneRed, Color(0xFFCC0000))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "InsaneTube Play",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(7.dp))

                // Brand Title
                Text(
                    text = "INSANETUBE",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp,
                        fontSize = 19.sp,
                        fontFamily = FontFamily.SansSerif
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (currentAccount?.isPremium == true) {
                    Spacer(modifier = Modifier.width(5.dp))
                    Surface(
                        color = Color(0xFF1E293B),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, Color(0xFF94A3B8).copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "PREMIUM",
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            // Action Icons Row (Cast, Bell with notification dot, Search, Avatar)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Cast Icon
                IconButton(
                    onClick = onCastClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("cast_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Cast,
                        contentDescription = "Cast to device",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Notifications Bell with Badge
                Box(contentAlignment = Alignment.TopEnd) {
                    IconButton(
                        onClick = onNotificationsClick,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("notifications_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Notification red dot badge
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp, end = 8.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(InsaneRed)
                    )
                }

                // Search Icon
                IconButton(
                    onClick = onSearchClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("search_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Profile Avatar / Incognito icon / Sign in
                if (currentAccount != null || isIncognito) {
                    ProfileAvatar(
                        name = currentAccount?.name ?: "Guest",
                        avatarColorHex = currentAccount?.avatarColorHex ?: 0xFFFF0033,
                        avatarUri = currentAccount?.avatarUri,
                        size = 32.dp,
                        hasBlueTick = currentAccount?.hasBlueTick == true,
                        isOwner = currentAccount?.isOwner == true,
                        isIncognito = isIncognito,
                        onClick = onProfileClick,
                        modifier = Modifier.testTag("profile_avatar")
                    )
                } else {
                    // Guest state: Prominent "Sign in" pill button
                    Button(
                        onClick = onProfileClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = InsaneRed.copy(alpha = 0.15f),
                            contentColor = InsaneRed
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .height(30.dp)
                            .testTag("topbar_signin_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sign in",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
