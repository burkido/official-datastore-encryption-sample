package com.example.encryptiondatastore.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.tink.AeadSerializer
import com.google.crypto.tink.Aead
import com.example.encryptiondatastore.data.SessionDataSerializer
import com.example.encryptiondatastore.data.di.qualifiers.ApplicationScope
import com.example.encryptiondatastore.data.di.qualifiers.Dispatcher
import com.example.encryptiondatastore.data.di.qualifiers.EdsDispatchers
import com.example.encryptiondatastore.data.model.SessionData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
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
        @Dispatcher(EdsDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        aead: Aead
    ): DataStore<SessionData> = DataStoreFactory.create(
        serializer = AeadSerializer(
            aead = aead,
            wrappedSerializer = SessionDataSerializer,
            associatedData = SESSION_DATASTORE_FILE.encodeToByteArray()
        ),
        scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
        produceFile = { context.dataStoreFile(SESSION_DATASTORE_FILE) }
    )
}
