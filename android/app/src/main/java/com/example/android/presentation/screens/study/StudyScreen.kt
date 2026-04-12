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
import androidx.compose.ui.platform.LocalContext
import com.example.android.domain.model.Flashcard
import com.example.android.presentation.screens.study.StudyViewModel
import com.example.android.presentation.util.SpeechManager

@Composable
fun StudyScreen(
    deckId: String, // Nhận ID bộ thẻ từ màn hình trước
    viewModel: StudyViewModel = hiltViewModel<StudyViewModel>(),
    onExit: () -> Unit
) {
    //ket noi su dung chuc nang Speech
    val context = LocalContext.current
    val speechManager = remember { SpeechManager(context.applicationContext) }

    //lắng nghe dữ liệu thật từ Database trả về
    val flashcards by viewModel.cardsToReview.collectAsState()
    var currentIndex by remember { mutableIntStateOf(0) }
    val total = flashcards.size
    val safeIndex = currentIndex.coerceIn(0, maxOf(0, flashcards.size - 1))
    LaunchedEffect(flashcards) {
        if (flashcards.isEmpty() && viewModel.isLoopingMode.value) {
            viewModel.resetSession() // Reset để học lại từ đầu
        }
        currentIndex = 0
    }

    //tu dong tat tranh ro ri bo nho
    DisposableEffect(Unit) {
        onDispose {
            speechManager.shutDown()
        }
    }

    Scaffold(
        topBar = {
            StudyHeader(
                current = if (total == 0) 0 else safeIndex + 1,
                total = total,
                onBack = onExit
            )
        },
        bottomBar = {
            if (flashcards.isNotEmpty()) {
               if (flashcards.size >= 10){
                   StudyActionButtons(
                       onAnswer = { quality ->
                           val currentCard = flashcards[safeIndex]
                           viewModel.answerCard(currentCard, quality)
                       }
                   )
               }
            }
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
                    },
                    onSpeak = { text -> speechManager.speak(text) }
                )
            }
        }
    }
}