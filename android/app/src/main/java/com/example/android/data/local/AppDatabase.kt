package com.example.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.data.local.entity.FlashcardEntity

@Database(
    entities = [FlashcardEntity::class, DeckEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()
{
    // Sau này anh em làm Data Access Object sẽ khai báo ở đây, ví dụ:
    // abstract fun flashcardDao(): FlashcardDao
}