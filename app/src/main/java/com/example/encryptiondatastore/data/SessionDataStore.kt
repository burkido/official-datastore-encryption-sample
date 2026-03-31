package com.example.encryptiondatastore.data

import androidx.datastore.core.DataStore
import com.example.encryptiondatastore.data.model.SessionData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionDataStore @Inject constructor(
    private val dataStore: DataStore<SessionData>
) : SessionRepository {

    override val sessionFlow: Flow<SessionData> = dataStore.data

    override suspend fun saveSession(email: String, token: String) {
        dataStore.updateData { SessionData(email = email, token = token) }
    }

    override suspend fun clearSession() {
        dataStore.updateData { SessionData() }
    }
}
