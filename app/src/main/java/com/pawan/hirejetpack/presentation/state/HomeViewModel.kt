package com.pawan.hirejetpack.presentation.state


import androidx.lifecycle.ViewModel
import com.pawan.hirejetpack.data.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * [HomeViewModel] — owns the job list state shown on the Home screen.
 *
 * Noob note: on creation it just loads the mock jobs into state once.
 * Staff note: this is exactly where a real implementation would instead
 * call a suspend repository function inside `viewModelScope.launch`,
 * setting `isLoading = true` first and catching failures into an error
 * field — the UI composable would not need to change at all either way,
 * since it only ever reacts to [HomeUiState].
 */
class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = HomeUiState(jobs = JobRepository.getJobs())
    }
}