package com.example.android.presentation.screens.study

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.android.domain.model.Flashcard
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun StackedFlashcardPager(
    cards: List<Flashcard>,
    onPageChange: (Int) -> Unit
) {
    if (cards.isEmpty()) return

    val total = cards.size
    var currentIndex by remember { mutableIntStateOf(0) }

    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val screenWidthPx = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val swipeThreshold = screenWidthPx * 0.2f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch { offsetX.snapTo(offsetX.value + dragAmount * 1.5f) }
                    },
                    onDragEnd = {
                        if (offsetX.value > swipeThreshold) {
                            scope.launch {
                                offsetX.animateTo(screenWidthPx, tween(250))
                                currentIndex = (currentIndex - 1 + total) % total
                                onPageChange(currentIndex)
                                offsetX.snapTo(0f)
                            }
                        } else if (offsetX.value < -swipeThreshold) {
                            scope.launch {
                                offsetX.animateTo(-screenWidthPx, tween(250))
                                currentIndex = (currentIndex + 1) % total
                                onPageChange(currentIndex)
                                offsetX.snapTo(0f)
                            }
                        } else {
                            scope.launch {
                                offsetX.animateTo(0f, tween(300))
                            }
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        val offsetVal = offsetX.value
        val isSwipingRight = offsetVal > 0

        val incomingIndex = if (isSwipingRight) {
            (currentIndex - 1 + total) % total
        } else {
            (currentIndex + 1) % total
        }

        val progress = (offsetVal / screenWidthPx).absoluteValue.coerceIn(0f, 1f)

        if (offsetVal != 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(480.dp)
                    .zIndex(0f)
                    .graphicsLayer {
                        val startPos = if (isSwipingRight) -screenWidthPx else screenWidthPx
                        translationX = startPos + offsetVal
                        scaleX = 1f
                        scaleY = 1f
                    },
                contentAlignment = Alignment.Center
            ) {
                FlashcardItem(card = cards[incomingIndex])
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(480.dp)
                .zIndex(1f)
                .graphicsLayer {
                    translationX = offsetVal
                    translationY = progress * 600f
                    rotationZ = (offsetVal / screenWidthPx) * 45f
                    transformOrigin = TransformOrigin(0.5f, 1f)
                },
            contentAlignment = Alignment.Center
        ) {
            FlashcardItem(card = cards[currentIndex])
        }
    }
}