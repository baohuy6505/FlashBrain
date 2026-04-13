package com.example.android.presentation.screens.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // 👈 Thêm cái này
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.domain.model.Notification

@Composable
fun NotificationItem(
    notification: Notification,
    timeText: String,
    isUnread: Boolean,
    icon: ImageVector,
    iconBgColor: Color,
    iconTintColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Icon Box
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconTintColor, modifier = Modifier.size(24.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.title,
                fontSize = 16.sp,
                fontWeight = if (isUnread) FontWeight.ExtraBold else FontWeight.Bold, // Đậm hơn nếu chưa đọc
                color = if (isUnread) Color.Black else Color.DarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notification.message,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Time & Unread Dot
        Column(horizontalAlignment = Alignment.End) {
            Text(text = timeText, fontSize = 11.sp, color = Color.LightGray)
            if (isUnread) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2196F3)) // Dùng màu xanh đồng bộ với App Huy nhé
                )
            }
        }
    }
}


data class IconUIData(
    val icon: ImageVector,
    val bgColor: Color,
    val tintColor: Color
)

@Composable
fun getIconDataForNotification(index: Int): IconUIData {
    return when (index % 5) {
        0 -> IconUIData(Icons.Default.LocalFireDepartment, Color(0xFFFFF3E0), Color(0xFFFF9800))
        1 -> IconUIData(Icons.Default.Style, Color(0xFFE8F5E9), Color(0xFF4CAF50))
        2 -> IconUIData(Icons.Default.Star, Color(0xFFE8EAF6), Color(0xFF3F51B5))
        3 -> IconUIData(Icons.Default.Notifications, Color(0xFFFFEBEE), Color(0xFFF44336))
        else -> IconUIData(Icons.Default.Schedule, Color(0xFFE0F2F1), Color(0xFF009688))
    }
}