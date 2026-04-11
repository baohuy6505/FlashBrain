package com.example.android.presentation.screens.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ScheduleCard(
    reviewCount: Int,
    onReviewClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth() // SỬA Ở ĐÂY: Thêm fillMaxWidth() để bung rộng ra
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Lệnh này giờ sẽ căn giữa hoàn hảo
        ) {
            Icon(Icons.Default.EventNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))

            Spacer(modifier = Modifier.height(12.dp))

            Text("Today's Schedule", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("You have $reviewCount cards to review now", color = Color.White.copy(alpha = 0.8f))

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onReviewClick() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(50)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Review Now", color = Color(0xFF1976D2))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}