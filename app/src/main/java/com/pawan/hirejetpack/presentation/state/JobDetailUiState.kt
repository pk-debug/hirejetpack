package com.pawan.hirejetpack.presentation.state

import com.pawan.hirejetpack.domain.Job

/**
 * [JobDetailUiState] — Single Source of Truth for the Job Detail screen.
 *
 * OOP Concept: Abstraction & Polymorphism
 * A sealed interface again, same reasoning as [LoginUiState]: the UI
 * polymorphically renders Loading / Found / NotFound instead of juggling
 * a nullable `Job?` plus a separate boolean flag that could disagree with
 * each other.
 */
sealed interface JobDetailUiState {
    object Loading : JobDetailUiState
    data class Found(
        val job: Job,
        val applicationStatus: ApplicationStatus = ApplicationStatus.Idle
    ) : JobDetailUiState
    object NotFound : JobDetailUiState
}