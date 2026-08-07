package com.pawan.hirejetpack.presentation.navigation

// ============================================================================
// SECTION 4: NAVIGATION & APP STRUCTURE
// ============================================================================

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pawan.hirejetpack.presentation.state.HomeViewModel
import com.pawan.hirejetpack.presentation.state.LoginViewModel
import com.pawan.hirejetpack.presentation.ui.home.HomeScreen
import com.pawan.hirejetpack.presentation.ui.login.LoginScreen
import com.pawan.hirejetpack.presentation.ui.profile.ProfileScreen

/**
 * KEYWORD: [@Composable]
 * Annotation informing the Kotlin compiler that this function transforms
 * data into UI nodes.
 *
 * KEYWORD: [rememberNavController]
 * Creates and retains a [androidx.navigation.NavHostController] instance
 * across Activity recompositions.
 *
 * KEYWORD: [NavHost] & [composable]
 * Defines the navigation graph, mapping route strings to Composable
 * destinations.
 *
 * Flow: Login → Home (job feed, lands here after login) → Profile
 * (reached via the drawer or the profile icon in the top bar).
 *
 * Note: [LoginViewModel] is created once, here, and passed down to both
 * Home and Profile screens — that's what lets Profile show the same
 * logged-in user's data without re-fetching or duplicating state.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true } // Clear backstack
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel()
            HomeScreen(
                homeViewModel = homeViewModel,
                loginViewModel = loginViewModel,
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onLogout = {
                    loginViewModel.resetState()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                viewModel = loginViewModel,
                onBack = { navController.popBackStack() },
                onLogout = {
                    loginViewModel.resetState()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}