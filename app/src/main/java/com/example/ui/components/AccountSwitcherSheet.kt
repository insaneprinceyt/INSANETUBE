package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserAccountEntity
import com.example.ui.theme.InsaneRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSwitcherSheet(
    isOpen: Boolean,
    accounts: List<UserAccountEntity>,
    currentAccount: UserAccountEntity?,
    isIncognito: Boolean,
    onDismiss: () -> Unit,
    onSelectAccount: (String) -> Unit,
    onAddAccountClick: () -> Unit,
    onToggleIncognito: () -> Unit,
    onSignOutClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onViewChannelClick: () -> Unit = {},
    onMonetizationClick: () -> Unit = {},
    onPremiumClick: () -> Unit = {},
    onChangeProfilePictureClick: (() -> Unit)? = null
) {
    if (!isOpen) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .testTag("account_switcher_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Accounts",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Accounts List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            ) {
                items(accounts, key = { it.id }) { acc ->
                    val isSelected = currentAccount?.id == acc.id

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectAccount(acc.id)
                            }
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                            .testTag("account_row_${acc.id}"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar
                        ProfileAvatar(
                            name = acc.name,
                            avatarColorHex = acc.avatarColorHex,
                            avatarUri = acc.avatarUri,
                            size = 44.dp,
                            hasBlueTick = acc.hasBlueTick,
                            isOwner = acc.isOwner
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        // User Info
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = acc.name,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                if (acc.hasBlueTick) {
                                    Box(
                                        modifier = Modifier
                                            .size(15.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF0284C7)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Verified Blue Tick",
                                            tint = Color.White,
                                            modifier = Modifier.size(10.dp)
                                        )
                                    }
                                } else if (acc.isVerified) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Verified",
                                        tint = InsaneRed,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }

                                if (acc.isOwner) {
                                    Surface(
                                        color = Color(0xFFF59E0B).copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "OWNER",
                                            color = Color(0xFFF59E0B),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }

                                if (acc.isPremium) {
                                    Surface(
                                        color = Color(0xFF6366F1).copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "PREMIUM",
                                            color = Color(0xFF818CF8),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "${acc.handle} • ${acc.subscribers}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = acc.email,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }

                        // Checkmark if selected
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Active account",
                                tint = InsaneRed,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                modifier = Modifier.padding(vertical = 6.dp)
            )

            // Quick Access Actions (Your Channel, Monetization, Premium)
            if (currentAccount != null) {
                AccountActionRow(
                    icon = Icons.Outlined.AccountBox,
                    title = "Your channel",
                    subtitle = "View subscriber count, videos & stats",
                    onClick = {
                        onDismiss()
                        onViewChannelClick()
                    },
                    testTag = "view_channel_action"
                )

                AccountActionRow(
                    icon = Icons.Default.AttachMoney,
                    title = "Monetization Studio",
                    subtitle = "Estimated revenue, ad streams & member perks",
                    onClick = {
                        onDismiss()
                        onMonetizationClick()
                    },
                    tint = Color(0xFF10B981),
                    testTag = "monetization_action"
                )

                AccountActionRow(
                    icon = Icons.Default.Stars,
                    title = "YouTube Premium",
                    subtitle = if (currentAccount.isPremium) "Membership active • Manage benefits" else "Ad-free, background play & 4K downloads",
                    onClick = {
                        onDismiss()
                        onPremiumClick()
                    },
                    tint = Color(0xFFF59E0B),
                    testTag = "premium_action"
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // Action Items (Add Account, Edit Profile, Incognito, Sign Out)
            AccountActionRow(
                icon = Icons.Default.Add,
                title = "Add account",
                subtitle = "Sign in or register a new channel",
                onClick = onAddAccountClick,
                testTag = "add_account_button"
            )

            if (currentAccount != null) {
                AccountActionRow(
                    icon = Icons.Outlined.Edit,
                    title = "Edit channel profile",
                    subtitle = "Update your channel name & handle",
                    onClick = onEditProfileClick,
                    testTag = "edit_profile_action"
                )

                if (onChangeProfilePictureClick != null) {
                    AccountActionRow(
                        icon = Icons.Outlined.PhotoCamera,
                        title = "Change profile picture",
                        subtitle = "Choose from gallery, creator presets or colors",
                        onClick = {
                            onDismiss()
                            onChangeProfilePictureClick()
                        },
                        tint = InsaneRed,
                        testTag = "change_profile_picture_action"
                    )
                }
            }

            AccountActionRow(
                icon = if (isIncognito) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                title = if (isIncognito) "Turn off Incognito" else "Turn on Incognito",
                subtitle = if (isIncognito) "Resume search and watch history" else "Browse privately without saving history",
                onClick = onToggleIncognito,
                testTag = "toggle_incognito_action"
            )

            if (currentAccount != null) {
                AccountActionRow(
                    icon = Icons.Outlined.ExitToApp,
                    title = "Use INSANETUBE signed out",
                    subtitle = "Browse as Guest without personal recommendations",
                    onClick = onSignOutClick,
                    tint = MaterialTheme.colorScheme.error,
                    testTag = "sign_out_action"
                )
            }
        }
    }
}

@Composable
private fun AccountActionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = tint
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
