package com.example.android.presentation.screens.study

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.presentation.screens.flashcard.FlashcardViewModel
import com.example.android.presentation.ui.theme.BgGray
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import com.example.android.domain.model.Flashcard
import com.example.android.presentation.screens.study.StudyViewModel

@Composable
fun StudyScreen(
    deckId: String, // Nhận ID bộ thẻ từ màn hình trước
    viewModel: StudyViewModel = hiltViewModel<StudyViewModel>(),
    onExit: () -> Unit
) {

    //Lắng nghe dữ liệu thật từ Database trả về
    val flashcards: List<Flashcard> by viewModel.cardsToReview.collectAsState(initial = emptyList())
    val total = flashcards.size
    var currentIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            StudyHeader(
                current = if (total == 0) 0 else currentIndex + 1,
                total = total,
                onBack = onExit
            )
        },
        containerColor = BgGray
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // 3. Nếu không có thẻ nào cần học hôm nay
            if (flashcards.isEmpty()) {
                Text(
                    text = "Chúc mừng! Bạn đã hoàn thành bài học hôm nay. 🎉",
                    color = Color.Gray
                )
            } else {
                // 4. Nếu có thẻ, truyền vào Pager của Huy
                StackedFlashcardPager(
                    cards = flashcards, // Dùng dữ liệu thật!
                    onPageChange = { newIndex ->
                        currentIndex = newIndex
                    }
                )
            }
        }
    }
}