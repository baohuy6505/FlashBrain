package com.example.android.presentation.screens.desk

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.domain.model.Deck

@Composable
fun DecksScreen(onDeckClick: (String) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val deckList = remember {
        listOf(
            Deck(
                id = "1",
                userId = "vinh",
                title = "Toán Cao Cấp",
                isPublic = true,
                createdAt = 1712650000000L // Giả lập ngày tạo
            ),
            Deck(
                id = "2",
                userId = "vinh",
                title = "Lập trình Android",
                isPublic = false,
                createdAt = 1712660000000L
            ),
            Deck(
                id = "3",
                userId = "vinh",
                title = "Tiếng Nga cơ bản",
                isPublic = true,
                createdAt = 1712670000000L
            ),
            Deck(
                id = "4",
                userId = "vinh",
                title = "Cơ sở dữ liệu",
                isPublic = true,
                createdAt = 1712680000000L
            )
        )
    }

    // Logic tìm kiếm
    val filteredDecks = deckList.filter {
        it.title.contains(searchQuery, ignoreCase = true) && !it.isDeleted
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Phần tiêu đề (Tổng số bộ thẻ, v.v...)
        DeckHeader()

        // Thanh tìm kiếm
        DeckSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Danh sách các bộ thẻ
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = filteredDecks,
                key = { it.id } // Dùng id làm key để LazyColumn mượt hơn
            ) { deck ->
                DeckListItem(
                    deck = deck,
                    onClick = { onDeckClick(deck.id) }
                )
            }
        }
    }
}