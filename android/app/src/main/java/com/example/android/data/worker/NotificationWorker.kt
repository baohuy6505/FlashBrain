package com.example.android.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.data.local.dao.NotificationDao
import com.example.android.data.local.entity.NotificationEntity
import com.example.android.data.local.SessionManager // Lấy userId từ đây
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.text.SimpleDateFormat
import java.util.*

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationDao: NotificationDao,
    private val sessionManager: SessionManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        // 1. Lấy ngày hiện tại
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = sdf.format(Date())

        // 2. Check số thẻ đến hạn
        val dueCount = notificationDao.countDueCards(today)

        if (dueCount > 0) {
            val userId = sessionManager.currentUser?.id ?: "guest"

            val notification = NotificationEntity(
                id = UUID.randomUUID().toString(),
                userId = userId,
                title = "Đã đến giờ ôn tập! ",
                message = "Huy ơi, bạn có $dueCount thẻ cần học hôm nay. Vào ôn ngay kẻo quên!",
                scheduledAt = today,
                isSent = false,
                createdAt = System.currentTimeMillis().toString()
            )

            // 3. Lưu vào máy & Bắn thông báo lên thanh trạng thái
            notificationDao.insertNotification(notification)
            sendVisualNotification(notification)

            // 4. Đánh dấu đã gửi
            notificationDao.updateNotification(notification.copy(isSent = true))
        }

        return Result.success()
    }

//su dung de demo thong bao
//override suspend fun doWork(): Result {
//    // Tạm thời ép dữ liệu ảo để test
//    val testNote = NotificationEntity(
//        id = UUID.randomUUID().toString(),
//        userId = "huy_test",
//        title = "Ting Ting! Code chạy rồi nè 🚀",
//        message = "Thông báo Local đã hoạt động nhé Huy. Giờ đi ngủ thôi!",
//        scheduledAt = "Now",
//        isSent = false,
//        createdAt = System.currentTimeMillis().toString()
//    )
//
//    // Bỏ qua IF, gọi thẳng hàm bắn thông báo
//    sendVisualNotification(testNote)
//
//    return Result.success()
//}


    private fun sendVisualNotification(note: NotificationEntity) {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "flashbrain_reminder"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Study Reminder", NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Huy thay bằng icon app nhé
            .setContentTitle(note.title)
            .setContentText(note.message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        manager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}