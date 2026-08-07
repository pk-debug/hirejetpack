package com.pawan.hirejetpack.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.pawan.hirejetpack.LoginContent
import com.pawan.hirejetpack.presentation.state.LoginUiState
import com.pawan.hirejetpack.presentation.state.LoginViewModel

// ============================================================================
// SECTION 5: LOGIN SCREEN COMPOSABLES
// ============================================================================

/**
 * Stateful Login Screen Container.
 *
 * KEYWORD: [collectAsState]
 * Converts a Kotlin Flow ([StateFlow]) into Compose [State]. Any emission from the Flow
 * forces this Composable (and its children) to recompose with new data.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Trigger navigation sideeffect when state turns to Success
    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onLoginSuccess()
        }
    }

    LoginContent(
        uiState = uiState,
        onLoginClick = { email, password ->
            viewModel.performLogin(email, password)
        }
    )
}
