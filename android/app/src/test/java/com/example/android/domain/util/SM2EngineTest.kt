package com.example.android.domain.util

package com.example.android.domain.util

import com.example.android.domain.model.Flashcard
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class SM2EngineTest {

    private val fixedNowString = "2026-04-13T10:00:00Z"
    private val fixedNowInstant = Instant.parse(fixedNowString)

    @Before
    fun setup() {
        mockkStatic(Instant::class)
        every { Instant.now() } returns fixedNowInstant
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // =========================
    // CREATE - FIRST LEARN
    // =========================
    @Test
    fun `Lần đầu - Easy (3)`() {
        val card = createCard(0, 0, 2.5)

        val result = SM2Engine.calculate(card, 3)

        assertEquals(1, result.repetition)
        assertEquals(1, result.interval)
        assertEquals(2.6, result.easeFactor, 0.001)

        assertEquals(fixedNowString, result.updatedAt)
        assertEquals(fixedNowString, result.lastReviewedAt)

        val expected = fixedNowInstant.plus(1, ChronoUnit.DAYS).toString()
        assertEquals(expected, result.nextReviewDate)
    }

    // =========================
    // SECOND LEARN
    // =========================
    @Test
    fun `Lần hai - Good (2)`() {
        val card = createCard(1, 1, 2.5)

        val result = SM2Engine.calculate(card, 2)

        assertEquals(2, result.repetition)
        assertEquals(6, result.interval)
        assertEquals(2.5, result.easeFactor, 0.001)

        val expected = fixedNowInstant.plus(6, ChronoUnit.DAYS).toString()
        assertEquals(expected, result.nextReviewDate)
    }

    // =========================
    // MULTIPLE REVIEW
    // =========================
    @Test
    fun `Lần N - Easy (3)`() {
        val card = createCard(2, 6, 2.5)

        val result = SM2Engine.calculate(card, 3)

        assertEquals(3, result.repetition)
        assertEquals(15, result.interval)
        assertEquals(2.6, result.easeFactor, 0.001)

        val expected = fixedNowInstant.plus(15, ChronoUnit.DAYS).toString()
        assertEquals(expected, result.nextReviewDate)
    }

    // =========================
    // AGAIN (0)
    // =========================
    @Test
    fun `Again (0) - reset`() {
        val card = createCard(5, 20, 2.5)

        val result = SM2Engine.calculate(card, 0)

        assertEquals(0, result.repetition)
        assertEquals(1, result.interval)
        assertEquals(2.18, result.easeFactor, 0.001)

        assertEquals(fixedNowString, result.updatedAt)
    }

    // =========================
    // HARD (1)
    // =========================
    @Test
    fun `Hard (1) - cũng reset`() {
        val card = createCard(3, 10, 2.5)

        val result = SM2Engine.calculate(card, 1)

        assertEquals(0, result.repetition)
        assertEquals(1, result.interval)
    }

    // =========================
    // MAX INTERVAL
    // =========================
    @Test
    fun `Interval không vượt 365`() {
        val card = createCard(10, 200, 2.5)

        val result = SM2Engine.calculate(card, 3)

        assertEquals(365, result.interval)
    }

    // =========================
    // MIN EASE FACTOR
    // =========================
    @Test
    fun `EaseFactor không dưới 1_3`() {
        val card = createCard(1, 1, 1.3)

        val result = SM2Engine.calculate(card, 0)

        assertEquals(1.3, result.easeFactor, 0.001)
    }

    // =========================
    // DATA INTEGRITY
    // =========================
    @Test
    fun `Không thay đổi field không liên quan`() {
        val card = createCard(1, 1, 2.5)

        val result = SM2Engine.calculate(card, 3)

        assertEquals("test_card", result.id)
        assertEquals("deck_123", result.deckId)
        assertEquals("Front", result.frontText)
        assertEquals("Back", result.backText)
    }

    // =========================
    // EDGE CASE
    // =========================
    @Test
    fun `Interval 0 nhưng repetition lớn`() {
        val card = createCard(3, 0, 2.5)

        val result = SM2Engine.calculate(card, 3)

        assertTrue(result.interval >= 1)
    }

    // =========================
    // HELPER
    // =========================
    private fun createCard(
        repetition: Int,
        interval: Int,
        easeFactor: Double
    ): Flashcard {
        return Flashcard(
            id = "test_card",
            deckId = "deck_123",
            frontText = "Front",
            backText = "Back",
            interval = interval,
            repetition = repetition,
            easeFactor = easeFactor,
            nextReviewDate = null,
            lastReviewedAt = null,
            createdAt = fixedNowString,
            updatedAt = fixedNowString,
            isDeleted = false
        )
    }
}