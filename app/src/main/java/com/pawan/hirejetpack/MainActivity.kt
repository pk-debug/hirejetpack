package com.pawan.hirejetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pawan.hirejetpack.presentation.state.LoginUiState
import com.pawan.hirejetpack.presentation.state.LoginViewModel
import com.pawan.hirejetpack.presentation.ui.login.LoginScreen
import com.pawan.hirejetpack.presentation.navigation.Screen


// ============================================================================
// SECTION 3: MAIN ACTIVITY ENTRY POINT
// ============================================================================

/**
 * KEYWORD: [ComponentActivity]
 * Base class for activities that use Jetpack Compose and modern Android architecture.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * KEYWORD: [setContent]
         * Composable bridge function that sets the Activity's root view to a Compose tree
         * replacing traditional XML layout inflation (`setContentView`).
         */
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}


/**
 * KEYWORD: [@Composable]
 * Annotation informing the Kotlin compiler that this function transforms data into UI nodes.
 *
 * KEYWORD: [rememberNavController]
 * Creates and retains a [NavHostController] instance across Activity recompositions.
 *
 * KEYWORD: [NavHost] & [composable]
 * Defines the navigation graph, mapping screen route strings to Composable destinations.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Login.route) { inclusive = true } // Clear backstack
                    }
                }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                viewModel = loginViewModel,
                onLogout = {
                    loginViewModel.resetState()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                }
            )
        }
    }
}


/**
 * Stateless Login Content Layout.
 *
 * KEYWORD: [remember]
 * Retains an object in memory across recompositions so it is not re-initialized.
 *
 * KEYWORD: [mutableStateOf]
 * Creates a reactive state value. Changing `.value` notifies Compose to redraw (recompose)
 * any composables reading this property.
 *
 * OOP Concept: Composition Over Inheritance
 * Compose UI is built by composing smaller UI blocks together instead of extending huge base view classes.
 */
@Composable
fun LoginContent(
    uiState: LoginUiState,
    onLoginClick: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    /**
     * KEYWORD: [Scaffold]
     * A structural container component providing slot layouts for standard Material Design
     * UI elements (TopBar, BottomBar, FloatingActionButton, Snackbar).
     */
    Scaffold { paddingValues ->
        /**
         * KEYWORDS: [Column], [Modifier], [Spacer]
         * - Column: Layout primitive that arranges children vertically.
         * - Modifier: Decorator pattern object used to alter composable styling, sizing, or touch behavior.
         * - Spacer: Blank layout component used for layout spacing.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState is LoginUiState.Error) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = { onLoginClick(email, password) },
                enabled = uiState !is LoginUiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState is LoginUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Login")
                }
            }
        }
    }
}

// ============================================================================
// SECTION 6: PROFILE SCREEN COMPOSABLES
// ============================================================================

/**
 * Profile Screen Component.
 */
@Composable
fun ProfileScreen(
    viewModel: LoginViewModel,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val user = (uiState as? LoginUiState.Success)?.user

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "User Profile",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            /**
             * KEYWORD: [Card]
             * Material design container surface for grouping related information.
             */
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Name: ${user?.name ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Email: ${user?.email ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Role: ${user?.role ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }
}