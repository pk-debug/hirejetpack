package com.pawan.hirejetpack.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pawan.hirejetpack.presentation.state.HomeViewModel
import com.pawan.hirejetpack.presentation.state.JobDetailViewModel
import com.pawan.hirejetpack.presentation.state.LoginViewModel
import com.pawan.hirejetpack.presentation.ui.home.HomeScreen
import com.pawan.hirejetpack.presentation.ui.jobdetail.JobDetailScreen
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
                onJobClick = { jobId -> navController.navigate(Screen.JobDetail.createRoute(jobId)) },
                onLogout = {
                    loginViewModel.resetState()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Screen.JobDetail.route,
            arguments = listOf(navArgument("jobId") { type = NavType.StringType })
        ) {
            // No need to read the argument here — JobDetailViewModel pulls
            // it from its injected SavedStateHandle instead.
            val jobDetailViewModel: JobDetailViewModel = viewModel()
            JobDetailScreen(
                viewModel = jobDetailViewModel,
                onBack = { navController.popBackStack() }
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