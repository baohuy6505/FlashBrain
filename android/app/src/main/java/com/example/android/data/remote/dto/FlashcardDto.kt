package com.example.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FlashcardDto(
    @SerializedName("_id") val id: String,
    @SerializedName("deck_id") val deckId: String,
    @SerializedName("front_text") val frontText: String,
    @SerializedName("back_text") val backText: String,
    val interval: Int,
    val repetition: Int,
    @SerializedName("ease_factor") val easeFactor: Double,
    @SerializedName("next_review_date") val nextReviewDate: String?,//chuoi ngay hoc lien tiep
    @SerializedName("last_reviewed_at") val lastReviewedAt: String?,
    val isDeleted: Boolean,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)