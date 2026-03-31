package com.example.encryptiondatastore.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.encryptiondatastore.ui.auth.AuthViewModel
import com.example.encryptiondatastore.ui.auth.HomeScreen
import com.example.encryptiondatastore.ui.auth.LoginScreen

/**
 * App navigation graph.
 *
 * Two routes:
 *  • "login" — [LoginScreen]
 *  • "home"  — [HomeScreen]
 *
 * A single [AuthViewModel] instance is shared between both screens because
 * they are siblings inside the same NavHost; hiltViewModel() returns the same
 * instance as long as the NavBackStackEntry is alive for the "login" route entry.
 *
 * Note: each composable() block creates its own NavBackStackEntry, so we hoist
 * the ViewModel at the NavGraph level would require a parent entry. For simplicity
 * we pass the ViewModel from each composable — both observe the same StateFlow
 * because Hilt scopes @HiltViewModel to the BackStackEntry, and navigation
 * pops the old entry before pushing the new one, which is fine here since
 * we popUpTo and remove entries on navigate.
 */
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            // hiltViewModel() is scoped to this backstack entry.
            LoginScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }

        composable("home") {
            HomeScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
    }
}
