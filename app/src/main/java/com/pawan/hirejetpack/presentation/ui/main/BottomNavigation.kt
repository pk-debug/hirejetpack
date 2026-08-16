package com.pawan.hirejetpack.presentation.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * [BottomNavItem] — the app's four top-level, always-visible destinations.
 *
 * OOP note: an `enum class` (rather than a sealed class, which [Screen]
 * uses) is the right tool specifically because these four tabs are a
 * fixed, exhaustive, ordered set that will never take a runtime
 * parameter — exactly what enums are for. [Screen.JobDetail] needs a
 * sealed class because it carries a `jobId` argument; nothing here does.
 * Picking between "enum" and "sealed class" isn't a style preference —
 * it should track whether the cases carry data or not.
 */
enum class BottomNavItem(val title: String, val icon: ImageVector) {
    Home("Job Feed", Icons.Filled.Home),
    Saved("Saved", Icons.Filled.Bookmark),
    Applications("Applications", Icons.Filled.Assignment),
    Profile("Profile", Icons.Filled.AccountCircle)
}