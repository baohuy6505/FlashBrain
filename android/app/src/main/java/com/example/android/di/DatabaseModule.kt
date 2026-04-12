package com.example.android.di

import android.content.Context
import androidx.room.Room
import com.example.android.data.local.AppDatabase
import com.example.android.data.local.dao.DeckDao
import com.example.android.data.local.dao.FlashcardDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Cung cấp các đối tượng tồn tại suốt vòng đời ứng dụng
object DatabaseModule {

    @Provides
    @Singleton // Chỉ tạo 1 bản duy nhất (Singleton)
    fun provideAppDatabase(
        @ApplicationContext context: Context // Hilt tự hiểu context này từ FlashBrainApp
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "flash_brain_db" // Tên file database trên điện thoại
        )
            .fallbackToDestructiveMigration() // Tạm thời dùng cái này để khi bạn đổi Entity nó tự xóa DB cũ tránh lỗi
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
}