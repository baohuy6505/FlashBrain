package com.example.android.data.mapper

import com.example.android.data.local.entity.DeckEntity
import com.example.android.data.remote.dto.DeckDto
import com.example.android.domain.model.Deck

// 1. Từ DTO (Atlas) -> Entity (Room)
// Dùng khi lấy danh sách bộ bài từ Server về máy
fun DeckDto.toEntity() = DeckEntity(
    id = id, // serverId của Atlas cũng chính là ID nếu Huy muốn đồng nhất
    userId = userId,
    title = title,
    isPublic = isPublic,
    isDeleted = isDeleted,
    isDirty = false, // Dữ liệu từ Server về là "sạch"
    serverId = id,
    createdAt = createdAt ?: "",
    updatedAt = updatedAt ?: ""
)

// 2. Từ Entity (Room) -> Domain (UI)
fun DeckEntity.toDomain() = Deck(
    id = id,
    userId = userId,
    title = title,
    isPublic = isPublic,
    isDeleted = isDeleted,
    isDirty = isDirty,
    serverId = serverId,
    createdAt = createdAt,
    updatedAt = updatedAt
)

// 3. Từ Domain (UI) -> DTO (Gửi lên Atlas)
fun Deck.toDto() = DeckDto(
    id = serverId ?: id, // Ưu tiên serverId nếu đã có
    userId = userId,
    title = title,
    isPublic = isPublic,
    isDeleted = isDeleted,
    createdAt = createdAt,
    updatedAt = updatedAt
)

// 4. Từ Domain (UI) -> Entity (Lưu máy)
fun Deck.toEntity() = DeckEntity(
    id = id,
    userId = userId,
    title = title,
    isPublic = isPublic,
    isDeleted = isDeleted,
    isDirty = isDirty,
    serverId = serverId,
    createdAt = createdAt,
    updatedAt = updatedAt
)