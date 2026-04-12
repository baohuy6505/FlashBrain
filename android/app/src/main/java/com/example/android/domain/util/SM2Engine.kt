package com.example.android.domain.util

import com.example.android.domain.model.Flashcard
import kotlin.math.max

object SM2Engine {
    fun calculate(card: Flashcard, quality: Int): Flashcard {
        // lấy thời điểm hiện tại dưới dạng Instant
        val nowInstant = java.time.Instant.now()
        val nowIso = nowInstant.toString()

        var newRepetition = card.repetition
        var newEaseFactor = card.easeFactor
        val newInterval: Int

        //nếu dùng thang 4 nút, thì chỉ cần quy đổi: 0:Again, 1:Hard, 2:Good, 3:Easy
        if (quality >= 2) { // Good hoặc Easy
            newInterval = when (newRepetition) {
                0 -> 1
                1 -> 6
                else -> (card.interval * newEaseFactor).toInt()
            }.coerceIn(1, 365)
            newRepetition += 1
        } else { // Again hoặc Hard
            newRepetition = 0
            newInterval = 1
        }

        // tính toán Ease Factor
        newEaseFactor += (0.1 - (3 - quality) * (0.08 + (3 - quality) * 0.02))
        newEaseFactor = newEaseFactor.coerceAtLeast(1.3)

        //cộng ngày CHUẨN bằng Instant
        val nextReviewInstant = nowInstant.plus(
            newInterval.toLong(),
            java.time.temporal.ChronoUnit.DAYS
        )

        return card.copy(
            repetition = newRepetition,
            easeFactor = newEaseFactor,
            interval = newInterval,
            nextReviewDate = nextReviewInstant.toString(),
            lastReviewedAt = nowIso,
            updatedAt = nowIso
        )
    }
}