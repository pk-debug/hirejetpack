package com.pawan.hirejetpack.presentation.state

import com.pawan.hirejetpack.domain.Job

/**
 * [ApplicationsUiState] — everything the Applications tab needs to
 * render.
 *
 * Staff note: [bookmarkedIds] is included here too, separately from
 * [jobs] — an applied job isn't necessarily a bookmarked one, so this
 * screen needs both pieces of shared state to correctly render each
 * [com.pawan.hirejetpack.presentation.ui.home.JobCard]'s bookmark icon.
 */
data class ApplicationsUiState(
    val jobs: List<Job> = emptyList(),
    val bookmarkedIds: Set<String> = emptySet()
)