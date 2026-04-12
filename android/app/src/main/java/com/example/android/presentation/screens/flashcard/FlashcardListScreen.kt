package com.example.android.presentation.screens.flashcard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
// Quan trọng: Đảm bảo import đúng đường dẫn đến Model
import com.example.android.domain.model.Flashcard
import com.example.android.presentation.screens.flashcard.FlashcardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardListScreen(
    deckId: String,
    onBack: () -> Unit,
    onReviewClick: () -> Unit,
    viewModel: FlashcardViewModel = hiltViewModel<FlashcardViewModel>()
) {
//    LaunchedEffect(deckId) {
//        if (deckId.isNotEmpty()) {
//            viewModel.setDeckId(deckId)
//        }
//    }
    val flashcards: List<Flashcard> by viewModel.uiState.collectAsState(initial = emptyList())
    val deck by viewModel.deckInfo.collectAsState(initial = null)

    var showSheet by remember { mutableStateOf(false) }
    var cardToEdit by remember { mutableStateOf<Flashcard?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(deck?.title?: "Loading...") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },

            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    cardToEdit = null // Reset về null để hiểu là thêm mới
                    showSheet = true  // Mở cái BottomSheet lên
                },
                containerColor = MaterialTheme.colorScheme.primary, // Màu xanh chủ đạo
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                // Sử dụng cái Icons.Default.Add mà Huy đã import
                Icon(imageVector = Icons.Default.Add, contentDescription = "Thêm thẻ mới")
            }
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
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                item {
                    deck?.let {
                        FlashcardListHeader(
                            deck = it,
                            totalCards = flashcards.size,
                            mastery = 65,
                            onReviewClick = onReviewClick
                        )
                    }
                }

                items(items = flashcards, key = { it.id }) { card ->
                    FlashcardListRow(
                        flashcard = card,
                        onEdit = {
                            cardToEdit = card
                            showSheet = true
                        },
                        onDelete = { viewModel.deleteCard(card.id) }
                    )
                }
            }
        }
        if (showSheet) {
            FlashcardActionBottomSheet(
                existingCard = cardToEdit, //truyền cái thẻ đang chọn (null nếu là thêm mới)
                onDismiss = { showSheet = false },
                onSave = { front, back ->
                    //gọi hàm addOrUpdateCard xịn xò ở ViewModel
                    viewModel.addOrUpdateCard(
                        existingCard = cardToEdit,
                        front = front,
                        back = back
                    )
                    showSheet = false // Lưu xong thì đóng sheet
                }
            )
        }
    }
}