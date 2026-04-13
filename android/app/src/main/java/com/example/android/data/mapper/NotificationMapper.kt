package com.example.android.data.mapper

import com.example.android.data.local.entity.NotificationEntity
import com.example.android.data.remote.dto.NotificationDto
import com.example.android.domain.model.Notification

// DTO (server) → Entity (local DB)
fun NotificationDto.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = this._id,
        userId = this.user_id,
        title = this.title,
        message = this.message,
        scheduledAt = this.scheduled_at,
        isSent = true,
        isRead = false,                 // Mặc định chưa đọc khi mới sync về
        createdAt = this.created_at
    )
}

// Entity (local DB) → Domain Model
fun NotificationEntity.toDomain(): Notification {
    return Notification(
        id = id,
        userId = userId,
        title = title,
        message = message,
        scheduledAt = scheduledAt,
        isSent = isSent,
        isRead = isRead,
        createdAt = createdAt
    )
}

// Domain Model → Entity (khi lưu local)
fun Notification.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        userId = userId,
        title = title,
        message = message,
        scheduledAt = scheduledAt,
        isSent = isSent,
        isRead = isRead,
        createdAt = createdAt
    )
}