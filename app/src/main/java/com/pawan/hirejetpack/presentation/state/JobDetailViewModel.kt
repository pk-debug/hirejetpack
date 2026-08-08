package com.pawan.hirejetpack.presentation.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pawan.hirejetpack.data.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
}