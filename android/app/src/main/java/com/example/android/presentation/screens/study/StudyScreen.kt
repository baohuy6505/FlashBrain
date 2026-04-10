package com.example.android.presentation.screens.study

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.android.domain.model.Flashcard
import com.example.android.presentation.ui.theme.BgGray

val dummyFlashcards = listOf(
    Flashcard(id = "1", deckId = "deck1", frontText = "The ability of neural networks in the brain to change through growth and reorganization.", backText = "Neuroplasticity"),
    Flashcard(id = "2", deckId = "deck1", frontText = "A cognitive bias where one relies too heavily on the first piece of information offered.", backText = "Anchoring Bias"),
    Flashcard(id = "3", deckId = "deck1", frontText = "The psychological phenomenon where people recall unfinished tasks better than completed ones.", backText = "Zeigarnik Effect"),
    Flashcard(id = "4", deckId = "deck1", frontText = "A mental shortcut that allows people to solve problems and make judgments quickly.", backText = "Heuristics"),
    Flashcard(id = "5", deckId = "deck1", frontText = "The state of having inconsistent thoughts, beliefs, or attitudes.", backText = "Cognitive Dissonance")
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