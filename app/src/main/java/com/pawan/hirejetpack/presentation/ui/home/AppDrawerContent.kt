package com.pawan.hirejetpack.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pawan.hirejetpack.domain.UserProfile
import com.pawan.hirejetpack.presentation.ui.components.InitialsAvatar

/**
 * [AppDrawerContent] — what's inside the slide-out panel.
 *
 * Noob note: [ModalDrawerSheet] is just a pre-styled Column with the right
 * width/elevation for a drawer — everything inside it is composed the
 * same way as any other screen.
 * Staff note: showing a profile summary (avatar + name) at the top of the
 * drawer is a standard pattern (Gmail, LinkedIn) — it reassures the user
 * which account they're in before they navigate anywhere else.
 */
@Composable
fun AppDrawerContent(
    user: UserProfile?,
    onProfileClick: () -> Unit,
    onHomeClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Profile summary header — tapping it opens the Profile screen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InitialsAvatar(name = user?.name ?: "Guest", size = 48.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = user?.name ?: "Guest User",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = user?.email ?: "Not signed in",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            NavigationDrawerItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                label = { Text("Home") },
                selected = true,
                onClick = onHomeClick
            )
            NavigationDrawerItem(
                icon = { Icon(Icons.Filled.AccountCircle, contentDescription = null) },
                label = { Text("Profile") },
                selected = false,
                onClick = onProfileClick
            )

            Spacer(modifier = Modifier.weight(1f))
            HorizontalDivider()

            NavigationDrawerItem(
                icon = { Icon(Icons.Filled.Logout, contentDescription = null) },
                label = { Text("Logout") },
                selected = false,
                onClick = onLogoutClick
            )
        }
    }
}