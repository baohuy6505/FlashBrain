package com.example.android.di

import com.example.android.data.repository.DeckRepositoryImpl
import com.example.android.data.repository.FlashcardRepositoryImpl
import com.example.android.data.repository.UserRepositoryImpl
import com.example.android.domain.repository.DeckRepository
import com.example.android.domain.repository.FlashcardRepository
import com.example.android.domain.repository.UserRepository
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
    abstract fun bindDeckRepository(
        deckRepositoryImpl: DeckRepositoryImpl
    ): DeckRepository

    @Binds
    @Singleton
    abstract fun bindFlashcardRepository(
        flashcardRepositoryImpl: FlashcardRepositoryImpl
    ): FlashcardRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}