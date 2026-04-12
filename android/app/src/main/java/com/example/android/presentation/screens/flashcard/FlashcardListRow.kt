package com.example.android.presentation.screens.flashcard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.QueryBuilder
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.domain.model.Flashcard

@Composable
fun FlashcardListRow(
    flashcard: Flashcard,
    onEdit: () -> Unit,
    onDelete: () -> Unit
    ) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = flashcard.frontText, // Sửa theo Model
                    modifier = Modifier.weight(1f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                //Icon(Icons.Default.DragIndicator, null, tint = Color.LightGray)
                IconButton(onClick = onEdit, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.DragIndicator, null, tint = Color.LightGray)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = flashcard.backText, // Sửa theo Model
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Hiển thị các chỉ số SM-2 thực tế
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FlashcardInfoTag(
                        icon = Icons.Default.QueryBuilder,
                        text = "Int: ${flashcard.interval}d"
                    )
                    FlashcardInfoTag(
                        icon = Icons.Default.ShowChart,
                        text = "Ease: ${String.format("%.1f", flashcard.easeFactor)}"
                    )
                }

                // --- THÊM CÁC NÚT THAO TÁC Ở ĐÂY ---
                Row {
                    TextButton(onClick = onEdit) {
                        Text("Sửa", color = Color(0xFF3F51B5))
                    }
                    TextButton(onClick = onDelete) {
                        Text("Xóa", color = Color(0xFFE53935))
                    }
                }
            }
        }
    }
}

@Composable
private fun FlashcardInfoTag(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Surface(color = Color(0xFFF5F7FF), shape = RoundedCornerShape(12.dp)) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(14.dp), tint = Color(0xFF3F51B5))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF3F51B5))
        }
    }
}