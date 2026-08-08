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

    /**
     * [JobDetail] — a parameterized route.
     *
     * `route` (with the `{jobId}` placeholder) is what you register with
     * `NavHost`. `createRoute(id)` is what you call when navigating TO a
     * specific job — it fills the placeholder in with a real value.
     * Splitting these two is what keeps the "template" and "an actual
     * destination" from being confused with each other at call sites.
     */
    object JobDetail : Screen("job_detail_screen/{jobId}") {
        fun createRoute(jobId: String) = "job_detail_screen/$jobId"
    }
}