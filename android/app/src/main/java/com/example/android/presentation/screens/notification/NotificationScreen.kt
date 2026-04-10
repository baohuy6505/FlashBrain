package com.example.android.presentation.screens.notification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.domain.model.Notification

@Composable
fun NotificationScreen(onNavigateBack: () -> Unit) {
    // 1. Khởi tạo danh sách Dữ liệu (vẫn là tĩnh nhưng ta sẽ quản lý trạng thái đọc riêng)
    val todayNotifications = remember {
        listOf(
            Notification(id = "1", userId = "u1", title = "Streak 15 ngày — xuất sắc!", message = "Bạn đang duy trì học tập liên tục. Tiếp tục nhé!", scheduledAt = 0),
            Notification(id = "2", userId = "u1", title = "42 thẻ cần ôn tập hôm nay", message = "Deck: Quantum Core, Advanced Neu...", scheduledAt = 0),
            Notification(id = "3", userId = "u1", title = "Nâng cấp Pro — ưu đãi 20%", message = "Chỉ còn 2 ngày cho gói Lifetime Master", scheduledAt = 0)
        )
    }

    val yesterdayNotifications = remember {
        listOf(
            Notification(id = "4", userId = "u1", title = "Nhắc nhở: bạn chưa học hôm qua", message = "Streak có thể bị mất nếu hôm nay bỏ lỡ", scheduledAt = 0),
            Notification(id = "5", userId = "u1", title = "Deck mới trong thư viện", message = "Behavioral Economics đã được thêm", scheduledAt = 0)
        )
    }

    // 2. STATE quản lý danh sách ID các thông báo ĐÃ ĐỌC
    // Ban đầu, giả sử ID "1" và "2" chưa đọc (không nằm trong set này)
    // Các ID từ "3" trở đi coi như đã đọc.
    var readNotificationIds by remember {
        mutableStateOf(setOf("3", "4", "5"))
    }

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            item {
                NotificationHeader(
                    onBackClick = onNavigateBack,
                    onMarkAllRead = {
                        // 3. KHI CLICK: Thêm TẤT CẢ ID vào danh sách đã đọc
                        val allIds = todayNotifications.map { it.id } + yesterdayNotifications.map { it.id }
                        readNotificationIds = readNotificationIds.plus(allIds)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // --- HÔM NAY ---
            item {
                Text("HÔM NAY", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(todayNotifications.size) { index ->
                val notif = todayNotifications[index]
                val time = when(index) {
                    0 -> "2 phút"
                    1 -> "1 giờ"
                    else -> "3 giờ"
                }
                val iconData = getIconDataForNotification(index)

                // 4. KIỂM TRA TRẠNG THÁI: Nếu ID chưa có trong set -> Chưa đọc
                val isUnread = !readNotificationIds.contains(notif.id)

                NotificationItem(
                    notification = notif,
                    timeText = time,
                    isUnread = isUnread,
                    icon = iconData.icon,
                    iconBgColor = iconData.bgColor,
                    iconTintColor = iconData.tintColor
                )
                HorizontalDivider(color = Color(0xFFF5F5F9))
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // --- HÔM QUA ---
            item {
                Text("HÔM QUA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(yesterdayNotifications.size) { index ->
                val notif = yesterdayNotifications[index]
                val iconData = getIconDataForNotification(index + 3)

                // Kiểm tra trạng thái (Thường hôm qua là đọc hết rồi)
                val isUnread = !readNotificationIds.contains(notif.id)

                NotificationItem(
                    notification = notif,
                    timeText = "Hôm qua",
                    isUnread = isUnread,
                    icon = iconData.icon,
                    iconBgColor = iconData.bgColor,
                    iconTintColor = iconData.tintColor
                )
                HorizontalDivider(color = Color(0xFFF5F5F9))
            }
        }
    }
}

// ... (Giữ nguyên phần helper getIconDataForNotification ở dưới cùng)
private data class IconUIData(val icon: androidx.compose.ui.graphics.vector.ImageVector, val bgColor: Color, val tintColor: Color)

private fun getIconDataForNotification(index: Int): IconUIData {
    return when (index) {
        0 -> IconUIData(Icons.Default.LocalFireDepartment, Color(0xFFFFF3E0), Color(0xFFFF9800)) // Streak
        1 -> IconUIData(Icons.Default.Style, Color(0xFFE8F5E9), Color(0xFF4CAF50)) // Flashcard
        2 -> IconUIData(Icons.Default.Star, Color(0xFFE8EAF6), Color(0xFF3F51B5)) // Pro
        3 -> IconUIData(Icons.Default.Notifications, Color(0xFFFFEBEE), Color(0xFFF44336)) // Reminder
        else -> IconUIData(Icons.Default.Schedule, Color(0xFFE0F2F1), Color(0xFF009688)) // Deck mới/Clock
    }
}