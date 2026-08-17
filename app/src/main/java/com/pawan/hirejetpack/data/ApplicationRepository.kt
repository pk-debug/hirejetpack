package com.pawan.hirejetpack.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * [ApplicationsRepository] — single source of truth for which job ids the
 * user has applied to.
 *
 * Staff note: shared between [com.pawan.hirejetpack.presentation.state.JobDetailViewModel]
 * (which submits the application) and
 * [com.pawan.hirejetpack.presentation.state.ApplicationsViewModel] (which
 * lists them on the Applications tab) — same singleton-`StateFlow` shape
 * as [BookmarkRepository] and [JobRepository]. Three repositories, one
 * recurring pattern: once you've built this shape once, recognizing where
 * it applies again is what "knowing the architecture" actually means in
 * practice, not just remembering the term "repository."
 */
object ApplicationsRepository {
    private val _appliedJobIds = MutableStateFlow<Set<String>>(emptySet())
    val appliedJobIds: StateFlow<Set<String>> = _appliedJobIds.asStateFlow()

    fun markApplied(jobId: String) {
        _appliedJobIds.value = _appliedJobIds.value + jobId
    }

    fun isApplied(jobId: String): Boolean = jobId in _appliedJobIds.value
}