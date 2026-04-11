package com.example.android.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues // ĐÃ THÊM IMPORT NÀY
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.presentation.screens.home.components.DeckItemHorizontal
import com.example.android.presentation.screens.home.components.ScheduleCard
import com.example.android.presentation.screens.home.components.WeeklyStatsCard
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.android.presentation.screens.home.components.ProBanner
import com.example.android.presentation.screens.home.components.StreakCard
import java.util.concurrent.ConcurrentNavigableMap

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateToDecks: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    // ĐÃ XÓA BỎ SCAFFOLD BỊ DƯ THỪA Ở ĐÂY
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 24.dp), // Thêm chút padding dưới cùng để list không bị dính sát đáy khi cuộn
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { Spacer(modifier = Modifier.height(10.dp)) }

        // 2. Thẻ hành trình
        item { StreakCard(state) }

        // 3. Thẻ lịch học hôm nay
        item {
            ScheduleCard(
                reviewCount = state.reviewCardCount,
                onReviewClick = onNavigateToDecks
            )
        }

        // 4. Mục My Decks Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("My Decks", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "View All >",
                    color = Color(0xFF1976D2),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.clickable { onNavigateToDecks() }
                )
            }
        }

        // 5. Danh sách ngang các Deck
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.recentDecks) { deck ->
                    DeckItemHorizontal(deck)
                }
            }
        }

        // 6. Weekly bieu do tuan
        item {
            WeeklyStatsCard(state = state)
        }

        // 7. Pro Banner
        item {
            ProBanner()
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}