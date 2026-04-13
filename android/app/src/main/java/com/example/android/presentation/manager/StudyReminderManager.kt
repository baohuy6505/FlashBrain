package com.example.android.presentation.manager


import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.android.data.worker.NotificationWorker
import com.example.android.domain.util.NotificationUtils
import java.util.concurrent.TimeUnit

object StudyReminderManager {

    fun startDailyReminders(context: Context) {
        val workManager = WorkManager.getInstance(context)

        val timeSlots = listOf(
            Triple(8, 0, "MORNING_REMINDER"),
            Triple(12, 0, "NOON_REMINDER"),
            Triple(21, 0, "NIGHT_REMINDER")
        )

        timeSlots.forEach { (hour, min, tag) ->
            val delay = NotificationUtils.calculateDelay(hour, min)

            val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag(tag)
                .build()
            workManager.enqueueUniquePeriodicWork(
                tag,
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )
        }
    }

    // Hàm này để user tắt hết thông báo khi họ muốn
    fun cancelAllReminders(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag("MORNING_REMINDER")
        WorkManager.getInstance(context).cancelAllWorkByTag("NOON_REMINDER")
        WorkManager.getInstance(context).cancelAllWorkByTag("NIGHT_REMINDER")
    }
}