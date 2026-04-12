package com.example.android.domain.util

import com.example.android.domain.model.Flashcard
import kotlin.math.max

object SM2Engine {
    fun calculate(card: Flashcard, quality: Int): Flashcard {
        val now = System.currentTimeMillis()
        var newRepetition = card.repetition
        var newEaseFactor = card.easeFactor

        val newInterval: Int
        if (quality >= 2) {
            newInterval = when (newRepetition) {
                0 -> 1
                1 -> 6
                else -> (card.interval * newEaseFactor).toInt()
            }.coerceIn(1, 365)
            newRepetition += 1
        } else {
            newRepetition = 0
            newInterval = 1
        }

        newEaseFactor += (0.1 - (3 - quality) * (0.08 + (3 - quality) * 0.02))
        newEaseFactor = newEaseFactor.coerceAtLeast(1.3)

        return card.copy(
            repetition = newRepetition,
            easeFactor = newEaseFactor,
            interval = newInterval,
            nextReviewDate = now + (newInterval * 86_400_000L),
            lastReviewedAt = now,
            updatedAt = now
        )
    }
}