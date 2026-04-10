package com.example.android.presentation.screens.auth.forgot_password

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ForgotPasswordHeader(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Spacer(modifier = Modifier.height(16.dp))

        // Nút Back
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp))
                .size(48.dp)
        ) {
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back", tint = Color(0xFF1E1E2C))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Icon Ổ khóa
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF5F5F9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Lock, contentDescription = "Lock", tint = Color(0xFF1E1E2C), modifier = Modifier.size(32.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Quên mật khẩu?",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1E1E2C)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Nhập email của bạn, chúng tôi sẽ gửi link đặt lại mật khẩu",
            fontSize = 14.sp,
            color = Color.Gray,
            lineHeight = 20.sp
        )
    }
}