package com.pawan.hirejetpack.presentation.state

import com.pawan.hirejetpack.domain.UserProfile


/**
 * [LoginUiState] Sealed Interface.
 *
 * OOP Concept: Abstraction & Polymorphism
 * Defines a strictly bounded set of distinct UI states. The UI layer polymorphically
 * handles whichever concrete state subclass is currently active.
 */
sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Success(val user: UserProfile) : LoginUiState
    data class Error(val message: String) : LoginUiState
}
