package com.example.android.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.android.presentation.screens.home.HomeState

@Composable
fun StreakCard(state: HomeState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Phần text bên trái
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "HANH TRÌNH CỦA BẠN",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified
                )
                Text(
                    text = "${state.streakDays} Days\nStreak",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    lineHeight = MaterialTheme.typography.headlineMedium.lineHeight * 0.8f
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Column {
                        Text("Total Cards", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text(
                            text = String.format("%, d", state.totalCardsLearned),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1976D2)
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        Text("Last Session", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text(text = state.lastSessionText, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Icon tia sét bên phải
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFFFFE082), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ElectricBolt,
                    contentDescription = null,
                    tint = Color(0xFFFBC02D),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}