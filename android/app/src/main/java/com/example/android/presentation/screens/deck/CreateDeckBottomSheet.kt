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
    existingDeck: Deck? = null, // null = Tạo mới, có data = Sửa
    onDismiss: () -> Unit,
    onSave: (Deck) -> Unit
) {
    // State lưu trữ Tên và Trạng thái Public/Private
    var deckName by remember { mutableStateOf(existingDeck?.title ?: "") }
    var isPublic by remember { mutableStateOf(existingDeck?.isPublic ?: false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isEditMode = existingDeck != null

    // Kiểm tra xem người dùng có thực sự thay đổi nội dung gì không để bật/tắt nút Lưu
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

            // --- KHUNG NHẬP TÊN ---
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

            // --- CÔNG TẮC CHỌN PUBLIC / PRIVATE ---
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
                        checkedTrackColor = Color(0xFF2196F3),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFD1D5DB),
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- NÚT LƯU / TẠO ---
            Button(
                onClick = {
                    if (canSave) {
                        if (isEditMode) {
                            // Cập nhật thẻ cũ
                            val updatedDeck = existingDeck!!.copy(
                                title = deckName.trim(),
                                isPublic = isPublic, // Lưu trạng thái mới
                                updatedAt = System.currentTimeMillis()
                            )
                            onSave(updatedDeck)
                        } else {
                            // Tạo thẻ mới
                            val newDeck = Deck(
                                id = UUID.randomUUID().toString(),
                                userId = "vinh",
                                title = deckName.trim(),
                                isPublic = isPublic, // Lưu trạng thái mới
                                createdAt = System.currentTimeMillis()
                            )
                            onSave(newDeck)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = canSave, // Tự động sáng/tối nút bấm theo logic ở trên
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