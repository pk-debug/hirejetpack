package com.pawan.hirejetpack.presentation.state

import androidx.lifecycle.ViewModel
import com.pawan.hirejetpack.domain.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ============================================================================
// SECTION 2: VIEWMODEL / STATE HOLDER (OOP Encapsulation & SRP)
// ============================================================================

/**
 * [LoginViewModel] manages UI business logic for authentication.
 *
 * OOP Concept: Single Responsibility Principle (SRP) & Encapsulation
 * Keeps business logic entirely separate from UI rendering logic.
 * Encapsulates mutable internal state [_uiState] behind a public, read-only [uiState].
 */
class LoginViewModel : ViewModel() {

    // Encapsulated internal mutable state (Private)
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)

    // Public read-only exposure of the state (Abstraction)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Simulates authentication request.
     */
    fun performLogin(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.value = LoginUiState.Error("Email and password cannot be empty.")
            return
        }

        _uiState.value = LoginUiState.Loading

        // Mock authentication success
        val user = UserProfile(
            name = "Pawan Kumar",
            email = email,
            role = "Android Developer"
        )
        _uiState.value = LoginUiState.Success(user)
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}
