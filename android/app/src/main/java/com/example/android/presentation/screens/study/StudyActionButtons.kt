package com.example.android.presentation.screens.study

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.presentation.ui.theme.PrimaryBlue

@Composable
fun StudyActionButtons(
    modifier: Modifier = Modifier,
    onAnswer: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Nút Lại (Quality = 0)
        ActionButton(
            text = "Lại",
            subText = "<1m",
            color = Color(0xFFE57373), // Màu đỏ nhạt
            modifier = Modifier.weight(1f),
            onClick = { onAnswer(0) }
        )
        // Nút Khó (Quality = 1)
        ActionButton(
            text = "Khó",
            subText = "2d",
            color = Color(0xFFFFB74D), // Màu cam
            modifier = Modifier.weight(1f),
            onClick = { onAnswer(1) }
        )
        // Nút Tốt (Quality = 2)
        ActionButton(
            text = "Tốt",
            subText = "4d",
            color = Color(0xFF81C784), // Màu xanh lá
            modifier = Modifier.weight(1f),
            onClick = { onAnswer(2) }
        )
        // Nút Dễ (Quality = 3)
        ActionButton(
            text = "Dễ",
            subText = "7d",
            color = Color(0xFF64B5F6), // Màu xanh dương
            modifier = Modifier.weight(1f),
            onClick = { onAnswer(3) }
        )
    }
}

@Composable
fun StudyNextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
    ) {
        Text(
            text = "Tiếp theo",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun ActionButton(
    text: String,
    subText: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(0.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = subText,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}