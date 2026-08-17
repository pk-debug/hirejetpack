package com.pawan.hirejetpack.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawan.hirejetpack.data.ApplicationsRepository
import com.pawan.hirejetpack.data.BookmarkRepository
import com.pawan.hirejetpack.data.JobRepository
import com.pawan.hirejetpack.domain.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * [ApplicationsViewModel] — the Applications tab's state, combining TWO
 * shared repositories: which jobs were applied to, and which are
 * bookmarked (so the same [com.pawan.hirejetpack.presentation.ui.home.JobCard]
 * can render correctly here too).
 *
 * Staff note: this is the same [combine] pattern as [HomeViewModel],
 * just with two data-layer flows instead of one data-layer flow plus one
 * local search flow. Once you've used `combine` once, recognizing "I have
 * two independent reactive inputs feeding one screen" becomes the trigger
 * for reaching for it again — that's the actual skill, more than the
 * operator itself.
 */
class ApplicationsViewModel : ViewModel() {

    private val allJobs: List<Job> = JobRepository.getJobs()

    val uiState: StateFlow<ApplicationsUiState> = combine(
        ApplicationsRepository.appliedJobIds,
        BookmarkRepository.bookmarkedIds
    ) { appliedIds, bookmarkedIds ->
        ApplicationsUiState(
            jobs = allJobs.filter { it.id in appliedIds },
            bookmarkedIds = bookmarkedIds
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ApplicationsUiState()
    )

    fun toggleBookmark(jobId: String) {
        BookmarkRepository.toggleBookmark(jobId)
    }
}