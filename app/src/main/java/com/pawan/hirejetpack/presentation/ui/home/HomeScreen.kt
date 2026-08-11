package com.pawan.hirejetpack.presentation.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pawan.hirejetpack.presentation.state.HomeViewModel
import com.pawan.hirejetpack.presentation.state.LoginUiState
import com.pawan.hirejetpack.presentation.state.LoginViewModel
import kotlinx.coroutines.launch

/**
 * [HomeScreen] — the app's landing screen after login: a drawer + job feed.
 *
 * KEYWORD: [ModalNavigationDrawer]
 * Wraps `content` and slides `drawerContent` in from the left edge when
 * `drawerState.isOpen` is true. It does NOT manage open/close state itself
 * — that's [rememberDrawerState] below, which is why it's local UI state
 * here and not something either ViewModel needs to know about.
 *
 * KEYWORD: [rememberDrawerState] / [DrawerState]
 * Holds whether the drawer is Open or Closed. `.open()` / `.close()` are
 * suspend functions, hence [rememberCoroutineScope] to call them from a
 * click handler.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    onNavigateToProfile: () -> Unit,
    onJobClick: (String) -> Unit,
    onLogout: () -> Unit
) {
    val homeState by homeViewModel.uiState.collectAsState()
    val loginState by loginViewModel.uiState.collectAsState()
    val user = (loginState as? LoginUiState.Success)?.user

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                user = user,
                onProfileClick = {
                    scope.launch { drawerState.close() }
                    onNavigateToProfile()
                },
                onHomeClick = {
                    scope.launch { drawerState.close() }
                },
                onLogoutClick = {
                    scope.launch { drawerState.close() }
                    onLogout()
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Job Feed") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Open navigation menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = onNavigateToProfile) {
                            Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                JobSearchField(
                    onQueryChanged = homeViewModel::onSearchQueryChanged,
                    onClear = homeViewModel::onClearSearch
                )

                when {
                    homeState.jobs.isNotEmpty() -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(homeState.jobs, key = { it.id }) { job ->
                                JobCard(job = job, onClick = { onJobClick(job.id) })
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
    }
}

/**
 * [JobSearchField] — the search input row.
 *
 * Staff note: this keeps its OWN local `remember { mutableStateOf(...) }`
 * for the text being typed, separate from [HomeUiState.searchQuery]. The
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