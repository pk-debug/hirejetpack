package com.pawan.hirejetpack.presentation.navigation

/**
 * [Screen] — sealed class defining every application route.
 *
 * OOP Concept: Type-Safe Abstraction
 * Using a sealed class instead of raw string constants means the compiler
 * catches typos in route names (`Screen.Home.route` vs. a mistyped
 * `"hom_screen"`) and every destination is enumerable in one place.
 */
sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Home : Screen("home_screen")
    object Profile : Screen("profile_screen")
}