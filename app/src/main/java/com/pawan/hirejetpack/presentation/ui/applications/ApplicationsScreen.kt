package com.pawan.hirejetpack.presentation.ui.applications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pawan.hirejetpack.presentation.state.ApplicationsViewModel
import com.pawan.hirejetpack.presentation.ui.home.JobCard

/**
 * [ApplicationsScreen] — the Applications tab: every job the user has
 * applied to, sourced from [com.pawan.hirejetpack.data.ApplicationsRepository]
 * via [ApplicationsViewModel]. Reuses the exact same [JobCard] as Home and
 * Saved — one leaf component, three different screens that happen to
 * render it, each supplying a different filtered list.
 */
@Composable
fun ApplicationsScreen(
    viewModel: ApplicationsViewModel,
    onJobClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.jobs.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("You haven't applied to any jobs yet.")
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
                    isBookmarked = job.id in uiState.bookmarkedIds,
                    onClick = { onJobClick(job.id) },
                    onBookmarkClick = { viewModel.toggleBookmark(job.id) }
                )
            }
        }
    }
}