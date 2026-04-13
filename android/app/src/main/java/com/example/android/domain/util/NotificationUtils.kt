package com.example.android.domain.util

import java.util.Calendar

object NotificationUtils {
    fun calculateDelay(targetHour: Int, targetMin: Int): Long {
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis

        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, targetHour)
            set(Calendar.MINUTE, targetMin)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Nếu giờ mục tiêu đã trôi qua trong ngày hôm nay
        // Ví dụ: Bây giờ là 10h sáng mà target là 8h sáng -> Hẹn sang 8h sáng mai
        if (target.timeInMillis <= now) {
            target.add(Calendar.DAY_OF_MONTH, 1)
        }

        return target.timeInMillis - now
    }
}