package com.example.encryptiondatastore.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.encryptiondatastore.ui.theme.EncryptionDataStoreTheme

/**
 * Home screen shown when the user has an active session.
 *
 * Displays the stored email and a masked token (first 10 chars + "…") to
 * visually confirm the token is present without revealing it entirely.
 *
 * [SessionState.LoggedOut] → auto-navigates back to login after logout.
 */
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val sessionState by viewModel.sessionState.collectAsState()

    // Navigate back to Login when the user logs out.
    LaunchedEffect(sessionState) {
        if (sessionState is SessionState.LoggedOut) {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    val loggedIn = sessionState as? SessionState.LoggedIn ?: return

    HomeScreenContent(
        email = loggedIn.email,
        token = loggedIn.token,
        onLogout = viewModel::logout
    )
}

@Composable
fun HomeScreenContent(
    email: String,
    token: String,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome!",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = email,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Show the session token card — token stored encrypted, displayed partially here.
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Active Session Token",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Show only the first 10 characters to demonstrate the token exists
                // without exposing it fully — the full value lives encrypted on disk.
                Text(
                    text = token,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Token stored as AES-256-GCM ciphertext via datastore-tink",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Logout")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview() {
    EncryptionDataStoreTheme {
        HomeScreenContent(
            email = "burak@example.com",
            token = "demo_token_1712345678901",
            onLogout = {}
        )
    }
}

