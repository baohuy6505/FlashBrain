package com.example.android.data.mapper

import com.example.android.data.local.entity.NotificationEntity
import com.example.android.data.remote.dto.NotificationDto
import com.example.android.domain.model.Notification

// DTO (server) → Entity (local DB)
fun NotificationDto.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = this.id,
        userId = this.userId,
        title = this.title,
        message = this.message,
        scheduledAt = this.dailyTime,
        isActive = this.isActive,
        isSent = true,
        isRead = false,
        createdAt = this.createdAt
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