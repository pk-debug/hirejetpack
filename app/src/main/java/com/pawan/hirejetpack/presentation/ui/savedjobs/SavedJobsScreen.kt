package com.pawan.hirejetpack.presentation.ui.savedjobs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pawan.hirejetpack.presentation.state.SavedJobsViewModel
import com.pawan.hirejetpack.presentation.ui.home.JobCard

/**
 * [SavedJobsScreen] — the Saved tab: every bookmarked job.
 *
 * Staff note: `isBookmarked = true` is hardcoded on every [JobCard] call
 * here. That's not a shortcut — it's correct by construction: everything
 * on this screen came FROM the bookmarked set (see [SavedJobsViewModel]),
 * so it can't be anything else. Tapping the icon un-bookmarks it, which
 * removes it from [SavedJobsViewModel.uiState] on the very next emission
 * — no manual "remove this item from my local list" code needed anywhere.
 */
@Composable
fun SavedJobsScreen(
    viewModel: SavedJobsViewModel,
    onJobClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.jobs.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No saved jobs yet — tap the bookmark icon on any job to save it here.")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.jobs, key = { it.id }) { job ->
                JobCard(
                    job = job,
                    isBookmarked = true,
                    onClick = { onJobClick(job.id) },
                    onBookmarkClick = { viewModel.toggleBookmark(job.id) }
                )
            }
        }
    }
}