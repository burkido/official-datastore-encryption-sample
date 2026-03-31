package com.example.encryptiondatastore.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.encryptiondatastore.data.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AuthViewModel"

/** Represents the three possible states of the user's session. */
sealed class SessionState {
    /** Initial state while the DataStore is being queried on first launch. */
    object Loading : SessionState()

    /** No valid session found — show Login screen. */
    object LoggedOut : SessionState()

    /** Valid session found — show Home screen. */
    data class LoggedIn(val email: String, val token: String) : SessionState()
}

/**
 * Shared ViewModel for Login and Home screens.
 *
 * On init it reads the DataStore once to decide the starting route.
 * Both screens observe [sessionState] and navigate automatically on state changes.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sessionDataStore: SessionRepository
) : ViewModel() {

    val sessionState: StateFlow<SessionState> = sessionDataStore.sessionFlow
        .map { session ->
            Log.d(TAG, "Session updated — token empty: ${session.token.isEmpty()}")
            if (session.token.isNotEmpty()) {
                SessionState.LoggedIn(email = session.email, token = session.token)
            } else {
                SessionState.LoggedOut
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SessionState.Loading
        )

    /**
     * Validates fields, generates a demo token, encrypts and persists the session,
     * then emits [SessionState.LoggedIn] to trigger navigation.
     *
     * @return true if login succeeded; false if validation failed (empty fields).
     */
    fun login(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) return false

        viewModelScope.launch {
            // Generate a fake token — in a real app this would come from your backend.
            val token = "demo_token_${System.currentTimeMillis()}"
            Log.d(TAG, "Login: saving encrypted session token=$token")
            sessionDataStore.saveSession(email = email.trim(), token = token)
        }
        return true
    }

    /** Clears the DataStore and emits [SessionState.LoggedOut] to trigger back-navigation. */
    fun logout() {
        viewModelScope.launch {
            Log.d(TAG, "Logout: clearing session")
            sessionDataStore.clearSession()
        }
    }
}
