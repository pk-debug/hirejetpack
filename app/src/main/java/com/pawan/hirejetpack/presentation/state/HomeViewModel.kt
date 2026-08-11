package com.pawan.hirejetpack.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawan.hirejetpack.data.JobRepository
import com.pawan.hirejetpack.domain.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * [HomeViewModel] — owns the job list state shown on the Home screen,
 * including search/filtering.
 *
 * Noob note: type in the search box, wait a beat, and the list filters
 * itself down to matching jobs.
 *
 * Staff note: [allJobs] is a plain, unchanging `List<Job>` — the mock
 * repository has no reason to be a Flow, since nothing external updates
 * it. Only [_searchQuery] needs to be reactive, because it changes on
 * every keystroke. [uiState] is *derived* from that one reactive source
 * via `debounce` → `distinctUntilChanged` → `map`, then converted back
 * into a hot `StateFlow` with [kotlinx.coroutines.flow.stateIn] so the UI
 * can `collectAsState()` it the same way as every other screen in this
 * app — the search logic is an implementation detail the Composable never
 * needs to know about.
 */
class HomeViewModel : ViewModel() {

    private val allJobs: List<Job> = JobRepository.getJobs()

    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<HomeUiState> = _searchQuery
        .debounce(300)                 // Wait for a pause in typing
        .distinctUntilChanged()        // Skip re-filtering on a no-op change
        .map { query -> filterJobs(query) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState(jobs = allJobs)
        )

    /**
     * Called on every keystroke from the search field. Cheap — it only
     * updates the query flow; the expensive filtering work happens
     * downstream, after debounce.
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onClearSearch() {
        _searchQuery.value = ""
    }

    private fun filterJobs(query: String): HomeUiState {
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
        return HomeUiState(jobs = filtered, searchQuery = query)
    }
}