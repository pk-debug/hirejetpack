package com.pawan.hirejetpack.presentation.state

import com.pawan.hirejetpack.domain.Job

/**
 * [SavedJobsUiState] — everything the Saved tab needs to render.
 *
 * Noob note: just a list of jobs — every job here is bookmarked, by
 * definition of how it's built (see [SavedJobsViewModel]).
 */
data class SavedJobsUiState(
    val jobs: List<Job> = emptyList()
)