package com.example.android.presentation.screens.desk

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.domain.model.Deck
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckActionBottomSheet(
    currentUserId: String,
    existingDeck: Deck? = null,
    onDismiss: () -> Unit,
    onSave: (Deck) -> Unit
) {
    var deckName by remember { mutableStateOf(existingDeck?.title ?: "") }
    var isPublic by remember { mutableStateOf(existingDeck?.isPublic ?: false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isEditMode = existingDeck != null

    // Logic kiểm tra nút Save
    val isNameChanged = deckName.trim() != (existingDeck?.title ?: "")
    val isStatusChanged = isPublic != (existingDeck?.isPublic ?: false)
    val canSave = deckName.isNotBlank() && (!isEditMode || isNameChanged || isStatusChanged)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 24.dp)
        ) {
            Text(
                text = if (isEditMode) "Chỉnh sửa bộ thẻ" else "Tạo bộ thẻ mới",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = deckName,
                onValueChange = { deckName = it },
                label = { Text("Tên bộ thẻ", color = Color.Gray) },
                placeholder = { Text("Ví dụ: Tiếng Nga cơ bản") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2196F3),
                    unfocusedBorderColor = Color(0xFFE5E7EB)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Công khai bộ thẻ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = if (isPublic) "Mọi người có thể tìm và xem bộ thẻ này" else "Chỉ mình bạn có thể xem bộ thẻ này",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Switch(
                    checked = isPublic,
                    onCheckedChange = { isPublic = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF2196F3)
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (canSave) {
                        // 👈 Chuyển thời gian sang String để khớp với Model
                        val currentTime = System.currentTimeMillis().toString()

                        if (isEditMode) {
                            val updatedDeck = existingDeck!!.copy(
                                title = deckName.trim(),
                                isPublic = isPublic,
                                isDirty = true,
                                updatedAt = currentTime
                            )
                            onSave(updatedDeck)
                        } else {
                            val newDeck = Deck(
                                id = UUID.randomUUID().toString(),
                                userId = currentUserId,
                                title = deckName.trim(),
                                isPublic = isPublic,
                                isDirty = true,
                                serverId = null,
                                createdAt = currentTime,
                                updatedAt = currentTime
                            )
                            onSave(newDeck)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = canSave,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3),
                    disabledContainerColor = Color(0xFF9CA3AF)
                )
            ) {
                Text(
                    text = if (isEditMode) "Lưu thay đổi" else "Tạo ngay",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}