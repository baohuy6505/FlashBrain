package com.example.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FlashcardDto(
    @SerializedName("_id") val id: String,
    @SerializedName("deckId") val deckId: String,
    @SerializedName("frontText") val frontText: String,
    @SerializedName("backText") val backText: String,
    val interval: Int,
    val repetition: Int,
    @SerializedName("easeFactor") val easeFactor: Double,
    @SerializedName("nextReviewDate") val nextReviewDate: String?,
    @SerializedName("lastReviewedAt") val lastReviewedAt: String?,
    val isDeleted: Boolean,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)