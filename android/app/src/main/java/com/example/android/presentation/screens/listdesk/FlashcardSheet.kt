package com.example.android.presentation.screens.listdesk

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Topic
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.domain.model.Flashcard
import com.example.android.presentation.ui.theme.*
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardSheet(
    deckId: String,
    existingCard: Flashcard? = null,
    onDismiss: () -> Unit,
    onSave: (Flashcard) -> Unit
) {
    var frontText by remember { mutableStateOf(existingCard?.frontText ?: "") }
    var backText by remember { mutableStateOf(existingCard?.backText ?: "") }
    val isEditMode = existingCard != null
    val canSave = frontText.isNotBlank() && backText.isNotBlank()

    // Khai báo công cụ để điều khiển cuộn màn hình
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = White,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = { BottomSheetDefaults.DragHandle(color = BorderGray) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .imePadding()
                .verticalScroll(scrollState)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- LOGO APP ---
            Box(
                modifier = Modifier
                    .size(84.dp)
                    .shadow(15.dp, CircleShape, ambientColor = BlueMain, spotColor = BlueMain)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(colors = listOf(BlueMain, Color(0xFF1E40AF)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ElectricBolt,
                    contentDescription = null,
                    tint = ProGold,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (isEditMode) "Chỉnh sửa thẻ" else "Tạo Flashcard mới",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = TextDark
            )

            Text(
                text = "Ghi nhớ kiến thức cùng FlashBrain",
                fontSize = 14.sp,
                color = TextLight,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
            )

            // --- Ô NHẬP MẶT TRƯỚC ---
            OutlinedTextField(
                value = frontText,
                onValueChange = { frontText = it },
                label = { Text("Mặt trước (Câu hỏi)") },
                leadingIcon = { Icon(Icons.Default.Topic, null, tint = BlueMain) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BlueMain,
                    unfocusedBorderColor = BorderGray,
                    focusedContainerColor = BgScreen,
                    unfocusedContainerColor = BgScreen
                )
            )

            Spacer(Modifier.height(16.dp))

            // --- Ô NHẬP MẶT SAU (CÓ TÍNH NĂNG TỰ CUỘN) ---
            OutlinedTextField(
                value = backText,
                onValueChange = { backText = it },
                label = { Text("Mặt sau (Câu trả lời)") },
                leadingIcon = { Icon(Icons.Default.QuestionAnswer, null, tint = BlueMain) },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        // Khi người dùng chạm vào ô này (focus), tự động cuộn xuống cuối
                        if (focusState.isFocused) {
                            scope.launch {
                                // Cuộn xuống thêm 300 pixel để lộ nút bấm
                                scrollState.animateScrollTo(scrollState.maxValue)
                            }
                        }
                    },
                minLines = 2,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BlueMain,
                    unfocusedBorderColor = BorderGray,
                    focusedContainerColor = BgScreen,
                    unfocusedContainerColor = BgScreen
                )
            )

            Spacer(Modifier.height(40.dp))

            // --- NÚT TẠO NGAY ---
            Button(
                onClick = {
                    if (canSave) {
                        val card = if (isEditMode) {
                            existingCard!!.copy(frontText = frontText.trim(), backText = backText.trim())
                        } else {
                            Flashcard(
                                id = UUID.randomUUID().toString(),
                                deckId = deckId,
                                frontText = frontText.trim(),
                                backText = backText.trim(),
                                interval = 0, repetition = 0, easeFactor = 2.5, isDeleted = false
                            )
                        }
                        onSave(card)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .shadow(10.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                enabled = canSave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueMain,
                    contentColor = White,
                    disabledContainerColor = BorderGray
                )
            ) {
                Text(
                    text = if (isEditMode) "LƯU THAY ĐỔI" else "TẠO THẺ NGAY",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    letterSpacing = 1.2.sp
                )
            }
        }
    }
}