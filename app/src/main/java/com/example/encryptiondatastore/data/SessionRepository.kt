package com.example.encryptiondatastore.data

import com.example.encryptiondatastore.data.model.SessionData
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    val sessionFlow: Flow<SessionData>
    suspend fun saveSession(email: String, token: String)
    suspend fun clearSession()
}
