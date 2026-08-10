package com.pawan.hirejetpack.presentation.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawan.hirejetpack.data.JobRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * [JobDetailViewModel] — owns the state for a single job's detail screen.
 *
 * KEYWORD: [SavedStateHandle]
 * Automatically injected by `viewModel()` when this ViewModel is created
 * inside a `composable(...)` destination that declares a nav argument.
 * It's a key-value bag holding the current destination's arguments AND
 * survives process death (unlike a constructor parameter would), which is
 * exactly why we read `jobId` from here instead of passing it straight
 * into the ViewModel's constructor from the Composable.
 *
 * Staff note: this ViewModel re-fetches the job from [JobRepository] using
 * only the id — it never trusts a `Job` object handed to it by the
 * previous screen. That's the whole point of passing ids through
 * navigation instead of whole objects: this ViewModel has exactly one
 * source of truth for "what is job X," no matter which screen navigated
 * here.
 */
class JobDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<JobDetailUiState>(JobDetailUiState.Loading)
    val uiState: StateFlow<JobDetailUiState> = _uiState.asStateFlow()

    init {
        val jobId: String? = savedStateHandle["jobId"]
        val job = jobId?.let { JobRepository.getJobById(it) }
        _uiState.value = if (job != null) {
            JobDetailUiState.Found(job)
        } else {
            JobDetailUiState.NotFound
        }
    }

    /**
     * Submits an application for the currently loaded job.
     *
     * Noob note: `delay(900)` is standing in for a real network call —
     * swap it for a suspend repository function later and nothing else
     * here changes.
     *
     * Staff note: the guard on `currentState is Found && applicationStatus
     * == Idle` prevents a double-submit if the user manages to tap twice
     * before recomposition disables the button — never rely on the UI
     * alone (like `enabled = false`) to make an action safe; the source
     * of truth should refuse the duplicate too.
     */
    fun applyToJob() {
        val currentState = _uiState.value
        if (currentState !is JobDetailUiState.Found) return
        if (currentState.applicationStatus != ApplicationStatus.Idle) return

        _uiState.value = currentState.copy(applicationStatus = ApplicationStatus.Submitting)

        viewModelScope.launch {
            delay(900) // Simulated network round-trip
            val latest = _uiState.value
            if (latest is JobDetailUiState.Found) {
                _uiState.value = latest.copy(applicationStatus = ApplicationStatus.Applied)
            }
        }
    }
}