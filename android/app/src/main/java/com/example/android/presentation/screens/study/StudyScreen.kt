package com.example.android.presentation.screens.study

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.android.domain.model.Flashcard
import com.example.android.presentation.ui.theme.BgGray

val dummyFlashcards = listOf(
    Flashcard(
        id = "1",
        deckId = "deck1",
        frontText = "Định nghĩa đạo hàm của hàm số f(x) tại điểm x0.",
        backText = "f'(x0) = lim (Δx->0) [f(x0 + Δx) - f(x0)] / Δx"
    ),
    Flashcard(
        id = "2",
        deckId = "deck1",
        frontText = "Ma trận vuông A được gọi là ma trận suy biến (singular matrix) khi nào?",
        backText = "Khi định thức của ma trận bằng 0 (det(A) = 0)."
    ),
    Flashcard(
        id = "3",
        deckId = "deck1",
        frontText = "Công thức tích phân từng phần đối với hai hàm số u(x) và v(x).",
        backText = "∫u dv = uv - ∫v du"
    ),
    Flashcard(
        id = "4",
        deckId = "deck1",
        frontText = "Điều kiện để một hệ phương trình tuyến tính AX = B có nghiệm duy nhất.",
        backText = "Ma trận hệ số A phải là ma trận vuông và không suy biến (det(A) ≠ 0)."
    ),
    Flashcard(
        id = "5",
        deckId = "deck1",
        frontText = "Khai triển Taylor của hàm f(x) tại lân cận điểm a.",
        backText = "f(x) = f(a) + f'(a)(x-a) + f''(a)/2! * (x-a)^2 + ... + f(n)(a)/n! * (x-a)^n + R_n(x)"
    )
)
@Composable
fun StudyScreen(onExit: () -> Unit) {
    val total = dummyFlashcards.size
    var currentIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            StudyHeader(
                current = currentIndex + 1,
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
            StackedFlashcardPager(
                cards = dummyFlashcards,
                onPageChange = { newIndex ->
                    currentIndex = newIndex
                }
            )
        }
    }
}