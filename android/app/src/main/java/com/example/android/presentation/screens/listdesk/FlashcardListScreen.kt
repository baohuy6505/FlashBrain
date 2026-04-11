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
// Quan trọng: Đảm bảo import đúng đường dẫn đến Model
import com.example.android.domain.model.DeckWithCards
import com.example.android.domain.model.Flashcard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardListScreen(data: DeckWithCards, onBack: () -> Unit, onReviewClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(data.deck.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },

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
                        onReviewClick = onReviewClick
                    )
                }

                // Sửa lỗi Argument type mismatch: Truyền trực tiếp list, không dùng .size
                items(data.flashcards) { card ->
                    FlashcardListRow(flashcard = card)
                }
            }
        }
    }
}