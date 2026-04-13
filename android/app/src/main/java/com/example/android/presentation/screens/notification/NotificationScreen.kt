package com.example.android.presentation.screens.notification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.domain.model.Notification
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NotificationScreen(
    onNavigateBack: () -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Phân nhóm: Hôm nay vs Hôm qua vs Cũ hơn
    val grouped = remember(uiState.notifications) {
        groupNotificationsByDate(uiState.notifications)
    }

    Scaffold(containerColor = Color.White) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
            ) {
                item {
                    NotificationHeader(
                        onBackClick = onNavigateBack,
                        onMarkAllRead = { viewModel.markAllAsRead() }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                grouped.forEach { (label, notifications) ->
                    item {
                        Text(
                            text = label.uppercase(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(notifications.size) { index ->
                        val notif = notifications[index]
                        val iconData = getIconDataForNotification(index)

                        NotificationItem(
                            notification = notif,
                            timeText = formatRelativeTime(notif.createdAt),
                            isUnread = !notif.isRead,
                            icon = iconData.icon,
                            iconBgColor = iconData.bgColor,
                            iconTintColor = iconData.tintColor,
                            onClick = { viewModel.markAsRead(notif.id) }
                        )
                        HorizontalDivider(color = Color(0xFFF5F5F9))
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }

                // Empty state
                if (uiState.notifications.isEmpty() && !uiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Không có thông báo nào", color = Color.Gray)
                        }
                    }
                }
            }

            // Loading indicator
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // Error snackbar nhẹ
            uiState.errorMessage?.let { msg ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) { Text(msg) }
            }
        }
    }
}

// Phân nhóm notification theo ngày
private fun groupNotificationsByDate(
    notifications: List<Notification>
): List<Pair<String, List<Notification>>> {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val today = formatter.format(Date())
    val calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    val yesterday = formatter.format(calendar.time)

    val grouped = notifications.groupBy { notif ->
        val dateStr = try { notif.createdAt.substring(0, 10) } catch (e: Exception) { "" }
        when (dateStr) {
            today -> "Hôm nay"
            yesterday -> "Hôm qua"
            else -> "Cũ hơn"
        }
    }

    // Đảm bảo thứ tự hiển thị đúng
    return listOf("Hôm nay", "Hôm qua", "Cũ hơn")
        .mapNotNull { label -> grouped[label]?.let { label to it } }
}

private fun formatRelativeTime(createdAt: String): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val date = sdf.parse(createdAt) ?: return "Vừa xong"
        val diffMs = System.currentTimeMillis() - date.time
        val diffMin = diffMs / 60_000
        when {
            diffMin < 1    -> "Vừa xong"
            diffMin < 60   -> "$diffMin phút"
            diffMin < 1440 -> "${diffMin / 60} giờ"
            else           -> "${diffMin / 1440} ngày"
        }
    } catch (e: Exception) { "" }
}