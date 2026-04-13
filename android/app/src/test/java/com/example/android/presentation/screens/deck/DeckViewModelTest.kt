package com.example.android.presentation.screens.deck

import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeckViewModelTest {

    private lateinit var viewModel: DeckViewModel
    private lateinit var repository: DeckRepository
    private lateinit var sessionManager: SessionManager

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk()
        sessionManager = mockk()

        every { sessionManager.currentUser?.id } returns "test_user_id_123"
        every { repository.allDecks } returns flowOf(emptyList())

        viewModel = DeckViewModel(repository, sessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    // =========================
    // CREATE NEW DECK
    // =========================
    @Test
    fun `addOrUpdateDeck - tạo deck mới`() = runTest {
        coEvery { repository.saveDeck(any()) } returns Unit

        viewModel.addOrUpdateDeck(
            existingDeck = null,
            title = "Toán Học",
            isPublic = true
        )

        advanceUntilIdle()

        val slot = slot<Deck>()
        coVerify(exactly = 1) { repository.saveDeck(capture(slot)) }

        val deck = slot.captured

        assertNotNull(deck.id)
        assertEquals("test_user_id_123", deck.userId)
        assertEquals("Toán Học", deck.title)
        assertTrue(deck.isPublic)
        assertTrue(deck.isDirty)
        assertFalse(deck.isDeleted)
        assertNull(deck.serverId)
        assertEquals(deck.createdAt, deck.updatedAt)
    }

    // =========================
    // UPDATE EXISTING DECK
    // =========================
    @Test
    fun `addOrUpdateDeck - cập nhật deck cũ`() = runTest {
        coEvery { repository.saveDeck(any()) } returns Unit

        val existingDeck = Deck(
            id = "old_id",
            userId = "test_user_id_123",
            title = "Lịch Sử",
            isPublic = false,
            isDirty = false,
            serverId = "server_123",
            createdAt = "1000",
            updatedAt = "1000",
            isDeleted = false
        )

        viewModel.addOrUpdateDeck(
            existingDeck = existingDeck,
            title = "Lịch Sử Cập Nhật",
            isPublic = true
        )

        advanceUntilIdle()

        val slot = slot<Deck>()
        coVerify { repository.saveDeck(capture(slot)) }

        val deck = slot.captured

        assertEquals("old_id", deck.id)
        assertEquals("Lịch Sử Cập Nhật", deck.title)
        assertTrue(deck.isPublic)
        assertTrue(deck.isDirty)
        assertEquals("server_123", deck.serverId)
        assertNotEquals("1000", deck.updatedAt) // phải được cập nhật
    }

    // =========================
    // DELETE DECK
    // =========================
    @Test
    fun `deleteDeck - gọi repository đúng id`() = runTest {
        val deckId = "deck_999"
        coEvery { repository.deleteDeck(any()) } returns Unit

        viewModel.deleteDeck(deckId)

        advanceUntilIdle()

        coVerify(exactly = 1) { repository.deleteDeck(deckId) }
    }

    // =========================
    // USER NULL -> GUEST
    // =========================
    @Test
    fun `addOrUpdateDeck - user null thì dùng guest`() = runTest {
        every { sessionManager.currentUser } returns null
        every { repository.allDecks } returns flowOf(emptyList())

        viewModel = DeckViewModel(repository, sessionManager)

        coEvery { repository.saveDeck(any()) } returns Unit

        viewModel.addOrUpdateDeck(
            existingDeck = null,
            title = "Guest Deck",
            isPublic = false
        )

        advanceUntilIdle()

        val slot = slot<Deck>()
        coVerify { repository.saveDeck(capture(slot)) }

        val deck = slot.captured
        assertEquals("guest", deck.userId)
    }

    // =========================
    // STATEFLOW TEST
    // =========================
    @Test
    fun `decksState - emit dữ liệu từ repository`() = runTest {
        val fakeDecks = listOf(
            Deck(
                id = "1",
                userId = "user",
                title = "Deck 1",
                isPublic = true,
                isDirty = false,
                serverId = null,
                createdAt = "1",
                updatedAt = "1",
                isDeleted = false
            )
        )

        every { repository.allDecks } returns flowOf(fakeDecks)

        viewModel = DeckViewModel(repository, sessionManager)

        val result = viewModel.decksState.first()

        assertEquals(fakeDecks, result)
    }
}