package com.example.android.presentation.screens.desk

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.domain.model.Deck
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DeckListItem(deck: Deck, onClick: () -> Unit) {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val dateString = sdf.format(Date(deck.createdAt))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Text("🗂️", fontSize = 20.sp)
            }

            Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(text = deck.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = if (deck.isPublic) Color(0xFFFFD600) else Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (deck.isPublic) "PUBLIC" else "PRIVATE",
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(dateString, fontSize = 10.sp, color = Color.Gray)
                }
            }
            Icon(Icons.Default.MoreVert, null, tint = Color.Gray)
        }
    }
}