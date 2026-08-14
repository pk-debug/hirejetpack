package com.pawan.hirejetpack.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawan.hirejetpack.data.BookmarkRepository
import com.pawan.hirejetpack.data.JobRepository
import com.pawan.hirejetpack.domain.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn

/**
 * [HomeViewModel] — owns the job list state shown on the Home screen:
 * search filtering AND bookmark status, combined into one [HomeUiState].
 *
 * Staff note: [uiState] is built from TWO independent reactive sources —
 * the debounced search query (owned by this ViewModel) and
 * [BookmarkRepository.bookmarkedIds] (owned by the data layer, shared
 * with the Job Detail screen). [combine] is the Flow operator for exactly
 * this situation: "recompute my output whenever EITHER input changes,"
 * without manually tracking which one changed or re-deriving state by
 * hand on every emission.
 */
class HomeViewModel : ViewModel() {

    private val allJobs: List<Job> = JobRepository.getJobs()

    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<HomeUiState> = combine(
        _searchQuery.debounce(300).distinctUntilChanged(),
        BookmarkRepository.bookmarkedIds
    ) { query, bookmarkedIds ->
        buildUiState(query = query, bookmarkedIds = bookmarkedIds)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(jobs = allJobs)
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onClearSearch() {
        _searchQuery.value = ""
    }

    /**
     * Delegates straight to the shared repository — this ViewModel does
     * not own bookmark state, it only triggers a change to it.
     */
    fun toggleBookmark(jobId: String) {
        BookmarkRepository.toggleBookmark(jobId)
    }

    private fun buildUiState(query: String, bookmarkedIds: Set<String>): HomeUiState {
        val filtered = if (query.isBlank()) {
            allJobs
        } else {
            allJobs.filter { job ->
                job.title.contains(query, ignoreCase = true) ||
                        job.company.contains(query, ignoreCase = true) ||
                        job.location.contains(query, ignoreCase = true) ||
                        job.tags.any { it.contains(query, ignoreCase = true) }
            }
        }
        return HomeUiState(jobs = filtered, searchQuery = query, bookmarkedIds = bookmarkedIds)
    }
}