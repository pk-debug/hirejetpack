package com.pawan.hirejetpack.presentation.ui.jobdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pawan.hirejetpack.presentation.state.JobDetailUiState
import com.pawan.hirejetpack.presentation.state.JobDetailViewModel

/**
 * [JobDetailScreen] — full details for a single job, reached by tapping a
 * [com.pawan.hirejetpack.presentation.ui.home.JobCard] on the Home feed.
 *
 * Noob note: this screen doesn't know *how* it got the job id — it just
 * asks its ViewModel for the current [JobDetailUiState] and renders
 * whichever branch is active (Loading / Found / NotFound).
 *
 * Staff note: handling `NotFound` explicitly (instead of just crashing on
 * a null job) matters more than it looks here. In a real app the id could
 * be stale — e.g. a deep link to a job that expired since the link was
 * shared — and a dedicated "no longer available" state is the difference
 * between a graceful screen and a crash report.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(
    viewModel: JobDetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is JobDetailUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is JobDetailUiState.NotFound -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("This job is no longer available.")
                }
            }

            is JobDetailUiState.Found -> {
                val job = state.job
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    MaterialTheme.colorScheme.secondaryContainer,
                                    RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Work,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                job.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(job.company, style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(job.location, style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "COMPENSATION",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                job.salary,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (job.tags.isNotEmpty()) {
                        Text(
                            "SKILLS",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            job.tags.forEach { tag ->
                                AssistChip(onClick = {}, label = { Text(tag) })
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { /* TODO: wire up a real apply flow */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Apply Now")
                    }
                }
            }
        }
    }
}