package com.example.android.di

import com.example.android.data.repository.DeckRepositoryImpl
import com.example.android.domain.repository.DeckRepository
import com.example.android.data.repository.FlashcardRepositoryImpl
import com.example.android.domain.model.Flashcard
import com.example.android.domain.repository.FlashcardRepository

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
    ): DeckRepository // Trả về Interface nhưng thực tế là Implementation

    @Binds
    @Singleton
    abstract fun bindFlashcardRepository(
        FlashcardRepositoryImpl: FlashcardRepositoryImpl
    ): FlashcardRepository
}