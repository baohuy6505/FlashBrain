package com.example.android.presentation.screens.study

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.domain.model.Flashcard
import com.example.android.presentation.ui.theme.*
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.VolumeUp

@Composable
fun FlashcardItem(
    card: Flashcard,
    onSpeak: (String) -> Unit
    ) {
    val animatedRotation = remember(card.id) { Animatable(0f) }
    var targetFaceBy180 by remember(card.id) { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(480.dp)
            .graphicsLayer {
                rotationY = animatedRotation.value
                cameraDistance = 12f * density
            }
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .height(480.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    scope.launch {
                        targetFaceBy180 += 180f
                        animatedRotation.animateTo(
                            targetValue = targetFaceBy180,
                            animationSpec = tween(durationMillis = 500)
                        )
                    }
                },
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            val isBackVisible = ((animatedRotation.value + 90f).toInt() / 180) % 2 != 0

            if (!isBackVisible) {
                StudyCardContent(
                    title = "DEFINITION",
                    text = card.frontText,
                    onSpeak = { onSpeak(card.frontText) },
                    tags = emptyList()
                )
            } else {
                Box(Modifier.fillMaxSize().graphicsLayer { rotationY = 180f }) {
                    StudyCardContent(
                        title = "ANSWER",
                        text = card.backText,
                        isAnswer = true,
                        onSpeak = { onSpeak(card.backText) }
                    )
                }
            }
        }
    }
}

@Composable
fun StudyCardContent(
    title: String,
    text: String,
    tags: List<String> = emptyList(),
    onSpeak: () -> Unit,
    isAnswer: Boolean = false
) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = title, color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                IconButton(onClick = onSpeak) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Read",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.LightGray.copy(0.3f), modifier = Modifier.size(24.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = text,
            fontSize = if (isAnswer) 28.sp else 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextBlack,
            modifier = Modifier.weight(1f)
        )
        if (tags.isNotEmpty() && !isAnswer) {
            Row {
                tags.forEach { tag ->
                    Surface(color = PrimaryBlue.copy(0.1f), shape = RoundedCornerShape(12.dp), modifier = Modifier.padding(end = 8.dp)) {
                        Text(text = tag, color = PrimaryBlue, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}