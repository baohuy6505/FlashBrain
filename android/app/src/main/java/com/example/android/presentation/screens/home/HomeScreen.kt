package com.example.android.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Scaffold
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
import androidx.compose.foundation.lazy.items // QUAN TRỌNG: Phải có dòng này
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.android.presentation.screens.home.components.ProBanner
import com.example.android.presentation.screens.home.components.StreakCard
import java.util.concurrent.ConcurrentNavigableMap
import androidx.compose.runtime.DisposableEffect

import androidx.lifecycle.Lifecycle

import androidx.lifecycle.LifecycleEventObserver

import androidx.compose.ui.platform.LocalLifecycleOwner
// Tạo một biến chứa dữ liệu mẫu để "đổ" vào HomeScreen

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    onNavigateToDecks: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            // Khi app được mở lại hoặc quay lại màn hình Home
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.fetchData()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    Scaffold(
        // GẮN THANH MENU DƯỚI ĐÂY
        bottomBar = {
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(10.dp)) }

            // 2. Thẻ hành trình
            item { StreakCard(state) }

            // 3. Thẻ lịch học hôm nay
//            item { ScheduleCard(
//                reviewCount = state.reviewCardCount,
//                onReviewClick = onNavigateToDecks
//                ) }

            // 4. Mục My Decks Header
//            item {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text("My Decks", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
//                    Text("View All >", color = Color(0xFF1976D2), style = MaterialTheme.typography.bodySmall)
//                }
//            }

//            // 5. Danh sách ngang các Deck
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(dummyDecks) { deck ->
                        DeckItemHorizontal(deck)
                    }
                }
            }
// 6. Weekly biểu đồ tuần
            // 6. Weekly biểu đồ tuần
            item {
                // Tạo một State giả chứa các con số đẹp để demo
                val mockStateForChart = state.copy(
                    weeklyStats = listOf(
                        WeeklyStat("T2", 5, false),
                        WeeklyStat("T3", 12, false),
                        WeeklyStat("T4", 8, false),
                        WeeklyStat("T5", 15, false),
                        WeeklyStat("T6", 20, false),
                        WeeklyStat("T7", 5, false),
                        WeeklyStat("CN", 2, true)
                    )
                )

                // Truyền cái State giả này vào là xong, không lỗi gạch đỏ!
                WeeklyStatsCard(state = mockStateForChart)
            }
            // 6. Weekly bieu do tuan: -> Them True khi ma >50
//            item {
//                WeeklyStatsCard(state = state)
//            }

            // 7. Pro Banner
            item {
                ProBanner()
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}