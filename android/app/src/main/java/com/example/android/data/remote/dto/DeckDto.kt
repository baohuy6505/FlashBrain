package com.example.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DeckDto(
    @SerializedName("_id") val id: String,
    val userId: String,
    val title: String,
    val isPublic: Boolean,
    val isDeleted: Boolean,
    val createdAt: String? = null,
    val updatedAt: String? = null
)