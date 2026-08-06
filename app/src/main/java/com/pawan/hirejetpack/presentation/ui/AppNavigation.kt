package com.pawan.hirejetpack.presentation.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pawan.hirejetpack.LoginScreen
import com.pawan.hirejetpack.LoginViewModel
import com.pawan.hirejetpack.ProfileScreen


// ============================================================================
// SECTION 4: NAVIGATION & APP STRUCTURE
// ============================================================================

/**
 * Sealed class defining application routes.
 * OOP Concept: Type-Safe Abstraction
 */
sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Profile : Screen("profile_screen")
}
