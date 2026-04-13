package com.example.android.di

import android.content.Context
import androidx.room.Room
import com.example.android.data.local.AppDatabase
import com.example.android.data.local.SessionManager
import com.example.android.data.local.dao.DeckDao
import com.example.android.data.local.dao.FlashcardDao
import com.example.android.data.local.dao.NotificationDao
import com.example.android.data.local.dao.UserProgressDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "flash_brain_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideDeckDao(database: AppDatabase): DeckDao {
        return database.deckDao()
    }

    @Provides
    fun provideFlashcardDao(database: AppDatabase): FlashcardDao {
        return database.flashcardDao()
    }

    @Provides
    @Singleton
    fun provideNotificationDao(database: AppDatabase): NotificationDao {
        return database.notificationDao()
    }

    // ĐƯA RA NGOÀI NÀY: Không để lồng trong hàm khác
    @Provides
    @Singleton
    fun provideSessionManager(
        @ApplicationContext context: Context
    ): SessionManager {
        return SessionManager(context)
    }

    @Provides
    @Singleton
    fun provideUserProgressDao(database: AppDatabase): UserProgressDao {
        return database.userProgressDao()
    }
}