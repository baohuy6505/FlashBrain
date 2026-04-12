package com.example.android.presentation.screens.flashcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.domain.model.Deck

@Composable
fun FlashcardListHeader(
    deck: Deck,
    totalCards: Int,
    mastery: Int,
    onReviewClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
        Button(
            onClick = onReviewClick, // Đã kết nối sự kiện
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Review Now", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("TOTAL CARDS", fontSize = 10.sp, color = Color.Gray)
                Text("$totalCards Cards", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Box(modifier = Modifier.height(30.dp).width(1.dp).background(Color.LightGray))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("MASTERY", fontSize = 10.sp, color = Color.Gray)
                Text("$mastery%", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3F51B5))
            }
        }
    }
}