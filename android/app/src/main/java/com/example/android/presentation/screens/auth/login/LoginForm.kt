package com.example.android.presentation.screens.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.presentation.ui.theme.PrimaryBlue

@Composable
fun LoginForm(
    email: String,                   // Lấy từ ViewModel
    onEmailChange: (String) -> Unit, // Đẩy về ViewModel
    password: String,                // Lấy từ ViewModel
    onPasswordChange: (String) -> Unit, // Đẩy về ViewModel
    onLoginClick: () -> Unit,        // Gọi hàm của ViewModel
    onForgotPasswordClick: () -> Unit,
    isLoading: Boolean               // Trạng thái chờ
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Email", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            enabled = !isLoading, // Khóa ô nhập khi đang đăng nhập
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFF1E1E2C)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Mật khẩu", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            enabled = !isLoading, // Khóa ô nhập khi đang đăng nhập
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFF1E1E2C)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Quên mật khẩu?",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E1E2C),
            modifier = Modifier
                .align(Alignment.End)
                .clickable(enabled = !isLoading) { onForgotPasswordClick() }
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button( // Đổi sang Button chính để hiện trạng thái Loading rõ hơn
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading, // Vô hiệu hóa nút khi đang xử lý
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Đăng nhập", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}