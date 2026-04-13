package com.example.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.data.local.dao.DeckDao
import com.example.android.data.local.dao.FlashcardDao
import com.example.android.data.local.dao.NotificationDao
import com.example.android.data.local.entity.FlashcardEntity
import com.example.android.data.local.entity.DeckEntity
import com.example.android.data.local.entity.NotificationEntity
import com.example.android.data.local.entity.UserEntity


@Database(
    entities = [
        FlashcardEntity::class,
        DeckEntity::class,
        UserEntity::class,
        NotificationEntity::class
               ],
    version = 1,
    exportSchema = false
)

//ket noi dao vao DB
abstract class AppDatabase : RoomDatabase()
{
    abstract fun flashcardDao(): FlashcardDao
    abstract fun deckDao(): DeckDao
    abstract fun notificationDao(): NotificationDao
}