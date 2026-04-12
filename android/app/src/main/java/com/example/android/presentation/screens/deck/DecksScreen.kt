package com.example.android.presentation.screens.flashcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android.domain.model.Deck
import com.example.android.presentation.screens.desk.DeckActionBottomSheet
import com.example.android.presentation.screens.desk.DeckHeader
import com.example.android.presentation.screens.desk.DeckListItem
import com.example.android.presentation.screens.desk.DeckSearchBar

@Composable
fun DecksScreen(
    viewModel: DeckViewModel = hiltViewModel(),
    onDeckClick: (String) -> Unit
) {
    //lay danh sach tu VM (da chuyen tu Flow sang State)
    val deckList by viewModel.decksState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    // State quản lý việc hiển thị BottomSheet và biết đang Sửa hay Tạo mới
    var showSheet by remember { mutableStateOf(false) }
    var deckToEdit by remember { mutableStateOf<Deck?>(null) }

    val filteredDecks by remember(deckList, searchQuery) {
        derivedStateOf {
            deckList.filter {
                it.title.contains(searchQuery, ignoreCase = true) && !it.isDeleted
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp)
        ) {
            DeckHeader()
            DeckSearchBar(query = searchQuery, onQueryChange = { searchQuery = it })
            Spacer(modifier = Modifier.height(12.dp))

            if (filteredDecks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(bottom = 100.dp)) {
                        Text("🛸", fontSize = 80.sp)
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Vũ trụ này thật trống trải...", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF455A64))
                        Text("Hãy tạo bộ thẻ để lấp đầy nó nhé!", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 88.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(items = filteredDecks, key = { it.id }) { deck ->
                        DeckListItem(
                            deck = deck,
                            onClick = { onDeckClick(deck.id) },
                            onEdit = {
                                deckToEdit = deck // Gán bộ thẻ cần sửa
                                showSheet = true  // Mở sheet
                            },
                            onDelete = { viewModel.deleteDeck(deck.id)}
                        )
                    }
                }
            }
        }

        ExtendedFloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 16.dp, bottom = 16.dp),
            onClick = {
                deckToEdit = null // Báo hiệu là tạo mới
                showSheet = true
            },
            containerColor = Color(0xFF2196F3),
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            icon = { Icon(Icons.Default.Add, null) },
            text = { Text("Thêm Thẻ") }
        )

        // --- HIỂN THỊ BOTTOM SHEET (DÙNG CHUNG CHO THÊM & SỬA) ---
        if (showSheet) {
            DeckActionBottomSheet(
                existingDeck = deckToEdit, // Nếu null là tạo mới, nếu có data là sửa
                onDismiss = { showSheet = false },
                onSave = { savedDeck ->
                       viewModel.addOrUpdateDeck(
                           id = deckToEdit?.id,
                           title = savedDeck.title,
                           isPublic = savedDeck.isPublic
                       )
                    showSheet = false // Đóng sheet
                }
            )
        }
    }
}
