package com.pawan.hirejetpack.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawan.hirejetpack.data.BookmarkRepository
import com.pawan.hirejetpack.data.JobRepository
import com.pawan.hirejetpack.domain.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * [SavedJobsViewModel] — derives the Saved tab's list purely from
 * [BookmarkRepository], with no local mutable state of its own.
 *
 * Staff note: notice there's no `_uiState` `MutableStateFlow` here, unlike
 * every other ViewModel in this app. It isn't needed — this screen's
 * ENTIRE output is a pure function of one upstream flow
 * (`bookmarkedIds -> filter allJobs`), so `.map { }.stateIn { }` covers
 * it completely. Reaching for a private mutable backing field out of
 * habit, when a `map`/`combine` chain can express the whole screen,
 * creates a second place the same information could live — avoid it.
 */
class SavedJobsViewModel : ViewModel() {

    private val allJobs: List<Job> = JobRepository.getJobs()

    val uiState: StateFlow<SavedJobsUiState> = BookmarkRepository.bookmarkedIds
        .map { bookmarkedIds ->
            SavedJobsUiState(jobs = allJobs.filter { it.id in bookmarkedIds })
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SavedJobsUiState()
        )

    fun toggleBookmark(jobId: String) {
        BookmarkRepository.toggleBookmark(jobId)
    }
}