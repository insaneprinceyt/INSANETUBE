package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NavTab
import com.example.ui.theme.InsaneRed

@Composable
fun TubeBottomNavBar(
    currentTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 6.dp,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                thickness = 0.5.dp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Home Tab
                NavBarItem(
                    label = "Home",
                    iconFilled = Icons.Filled.Home,
                    iconOutlined = Icons.Outlined.Home,
                    isSelected = currentTab == NavTab.HOME,
                    onClick = { onTabSelected(NavTab.HOME) },
                    testTag = "tab_home"
                )

                // Shorts Tab
                NavBarItem(
                    label = "Shorts",
                    iconFilled = Icons.Filled.Bolt,
                    iconOutlined = Icons.Outlined.Bolt,
                    isSelected = currentTab == NavTab.SHORTS,
                    onClick = { onTabSelected(NavTab.SHORTS) },
                    testTag = "tab_shorts"
                )

                // Create (+) Button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .border(1.2.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), CircleShape)
                        .clickable { onTabSelected(NavTab.CREATE) }
                        .testTag("tab_create"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Video or Short",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Subscriptions Tab
                NavBarItem(
                    label = "Subscriptions",
                    iconFilled = Icons.Filled.Subscriptions,
                    iconOutlined = Icons.Outlined.Subscriptions,
                    isSelected = currentTab == NavTab.SUBSCRIPTIONS,
                    onClick = { onTabSelected(NavTab.SUBSCRIPTIONS) },
                    testTag = "tab_subscriptions"
                )

                // You (Library) Tab
                NavBarItem(
                    label = "You",
                    iconFilled = Icons.Filled.VideoLibrary,
                    iconOutlined = Icons.Outlined.VideoLibrary,
                    isSelected = currentTab == NavTab.YOU,
                    onClick = { onTabSelected(NavTab.YOU) },
                    testTag = "tab_you"
                )
            }
        }
    }
}

@Composable
private fun NavBarItem(
    label: String,
    iconFilled: androidx.compose.ui.graphics.vector.ImageVector,
    iconOutlined: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isSelected) iconFilled else iconOutlined,
            contentDescription = label,
            tint = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            maxLines = 1
        )
    }
}
