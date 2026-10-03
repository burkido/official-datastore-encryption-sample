package com.example.encryptiondatastore.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.FileStorage
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.tink.AeadSerializer
import com.google.crypto.tink.Aead
import com.example.encryptiondatastore.data.SessionDataSerializer
import com.example.encryptiondatastore.data.di.qualifiers.ApplicationScope
import com.example.encryptiondatastore.data.model.SessionData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

private const val SESSION_DATASTORE_FILE = "session.json"

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideSessionDataStore(
        @ApplicationContext context: Context,
        @ApplicationScope scope: CoroutineScope,
        aead: Aead
    ): DataStore<SessionData> = DataStore.Builder(
        storage = FileStorage(
            serializer = AeadSerializer(
                aead = aead,
                wrappedSerializer = SessionDataSerializer,
                associatedData = SESSION_DATASTORE_FILE.encodeToByteArray()
            ),
            produceFile = { context.dataStoreFile(SESSION_DATASTORE_FILE) }
        ),
        context = scope.coroutineContext
    )
        // Decryption failures surface as CorruptionException, so an unreadable session resets to logged out.
        .setCorruptionHandler(ReplaceFileCorruptionHandler { SessionData() })
        .build()
}
