package com.example.android.presentation.screens.auth.forgot_password

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var isSent by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            ForgotPasswordHeader(onBackClick = onNavigateBack)

            Spacer(modifier = Modifier.height(32.dp))

            ForgotPasswordForm(
                email = email,
                onEmailChange = { email = it },
                onSubmitClick = {
                    if (email.isNotBlank()) {
                        isSent = true // Khi bấm gửi, hiện phần Feedback bên dưới
                    }
                }
            )

            if (isSent) {
                Spacer(modifier = Modifier.height(32.dp))
                ForgotPasswordFeedback(
                    email = email,
                    onResendClick = { /* Gọi API gửi lại ở đây */ },
                    onLoginClick = onNavigateToLogin
                )
            } else {
                Spacer(modifier = Modifier.height(48.dp))
                // Nếu chưa gửi, chỉ hiện nút quay lại đăng nhập ở dưới cùng
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Nhớ mật khẩu rồi? ", fontSize = 14.sp, color = Color.Gray)
                    Text(
                        text = "Đăng nhập",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E1E2C),
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}