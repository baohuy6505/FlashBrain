package com.example.android.presentation.screens.desk

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
import com.example.android.domain.model.Deck

@Composable
fun DecksScreen(onDeckClick: (String) -> Unit) {
    val deckList = remember {
        mutableStateListOf(
            Deck(id = "1", userId = "vinh", title = "Toán Cao Cấp", isPublic = true),
            Deck(id = "2", userId = "vinh", title = "Lập trình Android", isPublic = false)
        )
    }

    var searchQuery by remember { mutableStateOf("") }

    // State quản lý việc hiển thị BottomSheet và biết đang Sửa hay Tạo mới
    var showSheet by remember { mutableStateOf(false) }
    var deckToEdit by remember { mutableStateOf<Deck?>(null) }

    val filteredDecks = deckList.filter {
        it.title.contains(searchQuery, ignoreCase = true) && !it.isDeleted
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
                            onDelete = { deckList.remove(deck) }
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
                    if (deckToEdit == null) {
                        // Thêm mới
                        deckList.add(0, savedDeck)
                    } else {
                        // Sửa -> Tìm và cập nhật trong danh sách
                        val index = deckList.indexOfFirst { it.id == savedDeck.id }
                        if (index != -1) {
                            deckList[index] = savedDeck
                        }
                    }
                    showSheet = false // Đóng sheet
                }
            )
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DecksScreenPreview() {
    MaterialTheme {
        DecksScreen(
            onDeckClick = { /* Để trống vì Preview không cần xử lý click chuyển trang */ }
        )
    }
}