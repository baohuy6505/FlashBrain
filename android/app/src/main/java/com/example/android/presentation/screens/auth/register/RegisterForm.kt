package com.example.android.presentation.screens.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.presentation.ui.theme.PrimaryBlue

@Composable
fun RegisterForm(
    onRegisterClick: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Họ và tên
        Text(text = "Họ và tên", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFF1E1E2C)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email
        Text(text = "Email", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFF1E1E2C)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mật khẩu
        Text(text = "Mật khẩu", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFF1E1E2C)
            )
        )

        // Thanh đo độ mạnh mật khẩu
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val strength = when {
                password.isEmpty() -> 0
                password.length < 6 -> 1
                password.length < 8 -> 2
                else -> 3
            }

            val barModifier = Modifier.weight(1f).height(4.dp)
            Box(modifier = barModifier.background(if (strength >= 1) Color(0xFFFFA000) else Color.LightGray, RoundedCornerShape(2.dp)))
            Box(modifier = barModifier.background(if (strength >= 2) Color(0xFFFFA000) else Color.LightGray, RoundedCornerShape(2.dp)))
            Box(modifier = barModifier.background(if (strength >= 3) Color(0xFF4CAF50) else Color.LightGray, RoundedCornerShape(2.dp)))
            Box(modifier = barModifier.background(if (strength >= 4) Color(0xFF4CAF50) else Color.LightGray, RoundedCornerShape(2.dp)))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Checkbox điều khoản
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF1E1E2C))
            )

            val termsText = buildAnnotatedString {
                append("Tôi đồng ý với ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF1E1E2C))) {
                    append("Điều khoản")
                }
                append(" & ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF1E1E2C))) {
                    append("Chính sách")
                }
            }
            Text(text = termsText, fontSize = 12.sp, color = Color.DarkGray)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút Đăng ký
        OutlinedButton(
            onClick = { onRegisterClick(name, email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryBlue),
            enabled = isChecked
        ) {
            Text("Đăng ký", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}