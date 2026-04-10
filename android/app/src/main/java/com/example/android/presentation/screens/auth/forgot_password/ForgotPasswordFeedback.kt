package com.example.android.presentation.screens.auth.forgot_password

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ForgotPasswordFeedback(
    email: String,
    onResendClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    var timeLeft by remember { mutableIntStateOf(59) }

    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hộp thông báo đã gửi
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8F8FC), RoundedCornerShape(16.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Email đã được gửi tới", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(email, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1E2C))
                Spacer(modifier = Modifier.height(4.dp))
                Text("Kiểm tra hộp thư đến và spam", fontSize = 12.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Gửi lại (Đếm ngược)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Chưa nhận được? ", fontSize = 14.sp, color = Color.Gray)
            Text(
                text = if (timeLeft > 0) "Gửi lại (${timeLeft}s)" else "Gửi lại",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (timeLeft > 0) Color.Gray else Color(0xFF1E1E2C),
                modifier = Modifier.clickable(enabled = timeLeft == 0) {
                    timeLeft = 59 // Reset timer
                    onResendClick()
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quay về đăng nhập
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Nhớ mật khẩu rồi? ", fontSize = 14.sp, color = Color.Gray)
            Text(
                text = "Đăng nhập",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E2C),
                modifier = Modifier.clickable { onLoginClick() }
            )
        }
    }
}