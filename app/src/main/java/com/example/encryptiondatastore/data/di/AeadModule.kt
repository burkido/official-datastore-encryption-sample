package com.example.encryptiondatastore.data.di

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.PredefinedAeadParameters
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val SESSION_KEYSET_NAME = "session_keyset"
private const val SESSION_KEYSET_PREFS_FILE = "session_keyset_prefs"
private const val MASTER_KEY_URI = "android-keystore://master_key"

@Module
@InstallIn(SingletonComponent::class)
object AeadModule {

    @Provides
    @Singleton
    fun provideAead(@ApplicationContext context: Context): Aead {
        AeadConfig.register()
        return AndroidKeysetManager.Builder()
            .withSharedPref(context, SESSION_KEYSET_NAME, SESSION_KEYSET_PREFS_FILE)
            .withKeyTemplate(KeyTemplate.createFrom(PredefinedAeadParameters.AES256_GCM))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
            .getPrimitive(com.google.crypto.tink.RegistryConfiguration.get(), Aead::class.java)
    }
}
