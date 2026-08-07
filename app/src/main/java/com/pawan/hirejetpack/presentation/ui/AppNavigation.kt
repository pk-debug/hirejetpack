package com.pawan.hirejetpack.presentation.ui

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
