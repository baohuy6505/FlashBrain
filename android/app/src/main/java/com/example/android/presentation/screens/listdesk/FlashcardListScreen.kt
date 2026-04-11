package com.example.android.presentation.screens.listdesk

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.domain.model.DeckWithCards
import com.example.android.domain.model.Flashcard
import com.example.android.presentation.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardListScreen(
    data: DeckWithCards,
    onBack: () -> Unit,
    onReviewClick: () -> Unit,
    onDeleteCard: (Flashcard) -> Unit = {}
) {
    // Quản lý danh sách thẻ tại chỗ
    val flashcardsList = remember {
        mutableStateListOf<Flashcard>().apply { addAll(data.flashcards) }
    }

    var showSheet by remember { mutableStateOf(false) }
    var cardToEdit by remember { mutableStateOf<Flashcard?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = BgScreen,
            topBar = {
                TopAppBar(
                    title = { Text(data.deck.title, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = BgScreen)
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. Header hiển thị thông số (Mastery, Total)
                item {
                    FlashcardListHeader(
                        deck = data.deck,
                        totalCards = flashcardsList.size,
                        mastery = 68,
                        onReviewClick = onReviewClick
                    )
                }

                // 2. Hiển thị danh sách các thẻ
                items(flashcardsList, key = { it.id }) { card ->
                    FlashcardListRow(
                        flashcard = card,
                        onEdit = {
                            cardToEdit = it
                            showSheet = true
                        },
                        onDelete = { cardToDelete ->
                            flashcardsList.remove(cardToDelete)
                            onDeleteCard(cardToDelete)
                        }
                    )
                }

                // Khoảng trống an toàn ở cuối danh sách để nút FAB không che mất thẻ cuối cùng
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }

        // --- NÚT THÊM THẺ NỔI (ĐÃ NÂNG LÊN ĐỂ TRÁNH PHÍM ĐIỀU HƯỚNG) ---
        ExtendedFloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding() // Né thanh điều hướng hệ thống
                .padding(end = 16.dp, bottom = 32.dp), // Nâng cao thêm 32dp cho thoáng
            onClick = {
                cardToEdit = null // Trạng thái tạo mới
                showSheet = true
            },
            containerColor = BlueMain, // Màu xanh chủ đạo từ Color.kt
            contentColor = White,
            shape = RoundedCornerShape(16.dp),
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            text = { Text("Thêm Thẻ", fontWeight = FontWeight.Bold) }
        )
    }

    // Hiển thị FlashcardSheet (BottomSheet) khi người dùng muốn Thêm/Sửa
    if (showSheet) {
        FlashcardSheet(
            deckId = data.deck.id,
            existingCard = cardToEdit,
            onDismiss = {
                showSheet = false
                cardToEdit = null
            },
            onSave = { updatedCard ->
                val index = flashcardsList.indexOfFirst { it.id == updatedCard.id }
                if (index != -1) {
                    // Cập nhật thẻ hiện có
                    flashcardsList[index] = updatedCard
                } else {
                    // Thêm thẻ mới vào đầu danh sách
                    flashcardsList.add(0, updatedCard)
                }
                showSheet = false
                cardToEdit = null
            }
        )
    }
}