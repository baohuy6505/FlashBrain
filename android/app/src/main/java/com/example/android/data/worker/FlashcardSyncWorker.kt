package com.example.android.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.domain.repository.FlashcardRepository
import dagger.assisted.Assisted
import com.example.android.data.sync.DataSyncManager
import dagger.assisted.AssistedInject

@HiltWorker
class FlashcardSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: FlashcardRepository,
    private val syncManager: DataSyncManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            syncManager.syncDecks()
            syncManager.syncFlashcards()
            android.util.Log.d("HUY_DEBUG", "Worker: Tất cả dữ liệu đã được 'quét sạch', Atlas đã cập nhật!")
            Result.success()
        } catch (e: Exception) {
            android.util.Log.e("HUY_DEBUG", "Worker: Có lỗi xảy ra (${e.message}), đang chờ để thử lại sau.")
            Result.retry()
        }
    }
}