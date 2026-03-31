package com.example.encryptiondatastore.data.di

import com.example.encryptiondatastore.data.SessionDataStore
import com.example.encryptiondatastore.data.SessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSessionRepository(impl: SessionDataStore): SessionRepository
}
