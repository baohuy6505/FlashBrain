package com.example.android.presentation.screens.flashcard

import androidx.lifecycle.SavedStateHandle
import com.example.android.domain.model.Flashcard
import com.example.android.domain.repository.DeckRepository
import com.example.android.domain.repository.FlashcardRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class FlashcardViewModelTest {

    private lateinit var viewModel: FlashcardViewModel
    private lateinit var flashcardRepository: FlashcardRepository
    private lateinit var deckRepository: DeckRepository
    private lateinit var savedStateHandle: SavedStateHandle

    private val dispatcher = StandardTestDispatcher()
    private val deckId = "deck_123"

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        flashcardRepository = mockk(relaxed = true)
        deckRepository = mockk(relaxed = true)
        savedStateHandle = mockk()

        every { savedStateHandle.get<String>("deckId") } returns deckId

        mockkStatic(android.util.Log::class)
        every { android.util.Log.d(any(), any()) } returns 0
        every { android.util.Log.e(any(), any()) } returns 0

        mockkStatic(Instant::class)
        every { Instant.now() } returns Instant.parse("2026-04-13T10:00:00Z")

        viewModel = FlashcardViewModel(flashcardRepository, deckRepository, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    // =========================
    // INIT
    // =========================
    @Test
    fun `init - gọi scheduleSync`() {
        verify(exactly = 1) { flashcardRepository.scheduleSync() }
    }

    // =========================
    // CREATE
    // =========================
    @Test
    fun `addOrUpdateCard - tạo mới`() = runTest {
        viewModel.addOrUpdateCard(null, "A", "B")
        advanceUntilIdle()

        val slot = slot<Flashcard>()
        coVerify { flashcardRepository.insertOrUpdate(capture(slot)) }

        val card = slot.captured
        assertEquals(deckId, card.deckId)
        assertEquals("A", card.frontText)
        assertEquals("B", card.backText)

        // check time
        assertEquals("2026-04-13T10:00:00Z", card.createdAt)
        assertEquals(card.createdAt, card.updatedAt)
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    fun `addOrUpdateCard - update giữ nguyên logic spaced repetition`() = runTest {
        val old = Flashcard(
            id = "1",
            deckId = deckId,
            frontText = "old",
            backText = "old",
            interval = 10,
            repetition = 3,
            easeFactor = 2.7,
            nextReviewDate = "2026-04-10T10:00:00Z",
            lastReviewedAt = null,
            createdAt = "old_time",
            updatedAt = "old_time"
        )

        viewModel.addOrUpdateCard(old, "new", "new")
        advanceUntilIdle()

        val slot = slot<Flashcard>()
        coVerify { flashcardRepository.insertOrUpdate(capture(slot)) }

        val card = slot.captured

        assertEquals("1", card.id)
        assertEquals(10, card.interval)
        assertEquals(3, card.repetition)
        assertEquals(2.7, card.easeFactor)

        assertEquals("new", card.frontText)
        assertEquals("2026-04-13T10:00:00Z", card.updatedAt)
        assertNotEquals("old_time", card.updatedAt)
    }

    // =========================
    // DELETE
    // =========================
    @Test
    fun `deleteCard - gọi đúng repo`() = runTest {
        viewModel.deleteCard("abc")
        advanceUntilIdle()

        coVerify { flashcardRepository.softDelete("abc") }
    }

    // =========================
    // DECK ID EMPTY
    // =========================
    @Test
    fun `deckId rỗng - không insert`() = runTest {
        every { savedStateHandle.get<String>("deckId") } returns ""

        val vm = FlashcardViewModel(flashcardRepository, deckRepository, savedStateHandle)

        vm.addOrUpdateCard(null, "A", "B")
        advanceUntilIdle()

        coVerify(exactly = 0) { flashcardRepository.insertOrUpdate(any()) }
        verify { android.util.Log.e("HUY_DEBUG", any()) }
    }

    // =========================
    // FLOW TEST
    // =========================
    @Test
    fun `uiState - trả về list từ repo`() = runTest {
        val fake = listOf(createCard("1", "2026-04-10T10:00:00Z", false))

        coEvery { flashcardRepository.getCardsByDeck(deckId) } returns flowOf(fake)

        viewModel = FlashcardViewModel(flashcardRepository, deckRepository, savedStateHandle)

        val result = viewModel.uiState.first()
        assertEquals(1, result.size)
    }

    // =========================
    // REVIEW FILTER
    // =========================
    @Test
    fun `loadCardsToReview - lọc đúng`() = runTest {
        val now = "2026-04-13T10:00:00Z"

        val cards = listOf(
            createCard("1", "2026-04-10T10:00:00Z", false), // ok
            createCard("2", now, false), // ok
            createCard("3", "2026-04-15T10:00:00Z", false), // future
            createCard("4", "2026-04-10T10:00:00Z", true), // deleted
            createCard("5", null, false) // null
        )

        coEvery { flashcardRepository.getCardsByDeck(deckId) } returns flowOf(cards)

        viewModel.loadCardsToReview(deckId)
        advanceUntilIdle()

        val result = viewModel.cardsToReview.value

        assertEquals(2, result.size)
        assertTrue(result.all { it.id == "1" || it.id == "2" })
    }

    private fun createCard(id: String, next: String?, deleted: Boolean) = Flashcard(
        id = id,
        deckId = deckId,
        frontText = "f",
        backText = "b",
        interval = 1,
        repetition = 1,
        easeFactor = 2.5,
        nextReviewDate = next,
        lastReviewedAt = null,
        createdAt = "",
        updatedAt = "",
        isDeleted = deleted
    )
}