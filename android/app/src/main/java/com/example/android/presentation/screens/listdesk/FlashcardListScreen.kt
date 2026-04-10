package com.example.android.presentation.screens.listdesk

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.domain.model.Deck
import com.example.android.domain.model.DeckWithCards
import com.example.android.domain.model.Flashcard

// 1. TẠO DỮ LIỆU GIẢ LẬP
val dummyDeckWithCards = DeckWithCards(
    deck = Deck(id = "1", userId = "user1", title = "Toán Cao Cấp", isPublic = true),
    flashcards = listOf(
        Flashcard(id = "101", deckId = "1", frontText = "Đạo hàm của sin(x) là gì?", backText = "cos(x)", interval = 1, easeFactor = 2.5),
        Flashcard(id = "102", deckId = "1", frontText = "Tích phân của e^x là gì?", backText = "e^x + C", interval = 3, easeFactor = 2.6),
        Flashcard(id = "103", deckId = "1", frontText = "Công thức Euler?", backText = "e^(iπ) + 1 = 0", interval = 0, easeFactor = 2.5)
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardListScreen(
    data: DeckWithCards = dummyDeckWithCards, // Dùng dữ liệu giả lập làm mặc định
    onBack: () -> Unit,
    onReviewClick: () -> Unit // 2. Thêm sự kiện để chuyển qua màn hình Study
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(data.deck.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    FlashcardListHeader(
                        deck = data.deck,
                        totalCards = data.flashcards.size,
                        mastery = 68,
                        onReviewClick = onReviewClick // Truyền sự kiện xuống Header
                    )
                }

                items(data.flashcards) { card ->
                    FlashcardListRow(flashcard = card)
                }
            }
        }
    }
}