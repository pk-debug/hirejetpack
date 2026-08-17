package com.pawan.hirejetpack.presentation.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pawan.hirejetpack.presentation.state.HomeViewModel

/**
 * [HomeScreenContent] — the Job Feed tab's body: search bar + job list.
 *
 * Staff note: this composable has NO Scaffold and NO TopAppBar of its
 * own — [com.pawan.hirejetpack.presentation.ui.main.MainScreen] owns the
 * single shared Scaffold (top bar + bottom nav) that wraps every tab.
 * Nesting a second Scaffold in here would draw a redundant app bar and
 * fight MainScreen for the window insets it already consumed. This is
 * the same "dumb, composed leaf" instinct as [JobCard], just applied at
 * the screen level instead of the row level — the previous version of
 * this file also owned a `ModalNavigationDrawer`; that responsibility
 * moved to the bottom nav in MainScreen entirely, so it's gone from here.
 */
@Composable
fun HomeScreenContent(
    viewModel: HomeViewModel,
    onJobClick: (String) -> Unit
) {
    val homeState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        JobSearchField(
            onQueryChanged = viewModel::onSearchQueryChanged,
            onClear = viewModel::onClearSearch
        )

        when {
            homeState.jobs.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(homeState.jobs, key = { it.id }) { job ->
                        JobCard(
                            job = job,
                            isBookmarked = job.id in homeState.bookmarkedIds,
                            onClick = { onJobClick(job.id) },
                            onBookmarkClick = { viewModel.toggleBookmark(job.id) }
                        )
                    }
                }
            }

            homeState.searchQuery.isNotBlank() -> {
                // Jobs exist overall, this specific search just has no matches
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No jobs match \"${homeState.searchQuery}\".")
                }
            }

            else -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No jobs available right now.")
                }
            }
        }
    }
}

/**
 * [JobSearchField] — the search input row.
 *
 * Staff note: this keeps its OWN local `remember { mutableStateOf(...) }`
 * for the text being typed, separate from `HomeUiState.searchQuery`. The
 * ViewModel's copy only updates after the 300ms debounce, but the
 * TextField itself must reflect every keystroke immediately — otherwise
 * the user would see their own typing lag behind their finger. Local UI
 * state and debounced app state serving two different jobs, on purpose.
 */
@Composable
private fun JobSearchField(
    onQueryChanged: (String) -> Unit,
    onClear: () -> Unit
) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onQueryChanged(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text("Search title, company, location, skill...") },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (text.isNotEmpty()) {
                IconButton(onClick = {
                    text = ""
                    onClear()
                }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Clear search")
                }
            }
        }
    )
}