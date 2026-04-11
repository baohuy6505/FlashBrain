package com.example.android.presentation.screens.listdesk

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.domain.model.Flashcard
import com.example.android.presentation.ui.theme.*

@Composable
fun FlashcardListRow(
    flashcard: Flashcard,
    onEdit: (Flashcard) -> Unit,
    onDelete: (Flashcard) -> Unit
) {
    // Biến trạng thái để đóng/mở menu ba chấm
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = flashcard.frontText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = flashcard.backText,
                        fontSize = 14.sp,
                        color = TextLight,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Nút Ba chấm và Menu lựa chọn
                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = TextLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Menu hiện ra khi nhấn nút ba chấm
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(White)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sửa thẻ") },
                            leadingIcon = {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                            },
                            onClick = {
                                showMenu = false
                                onEdit(flashcard)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Xóa thẻ", color = Color.Red.copy(0.8f)) },
                            leadingIcon = {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red.copy(0.8f), modifier = Modifier.size(18.dp))
                            },
                            onClick = {
                                showMenu = false
                                onDelete(flashcard)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FlashcardInfoTag(Icons.Default.Update, "Int: ${flashcard.interval}d")
                FlashcardInfoTag(Icons.Default.TrendingUp, "Ease: ${String.format("%.1f", flashcard.easeFactor)}")
            }
        }
    }
}

@Composable
private fun FlashcardInfoTag(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Surface(
        color = BlueMain.copy(alpha = 0.08f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(12.dp), tint = BlueMain)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = BlueMain)
        }
    }
}