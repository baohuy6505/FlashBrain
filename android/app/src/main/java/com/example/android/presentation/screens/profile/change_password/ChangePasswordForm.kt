package com.example.android.presentation.screens.profile.change_password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChangePasswordForm(onSaveClick: (String, String) -> Unit) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var currentPwVisible by remember { mutableStateOf(false) }
    var newPwVisible by remember { mutableStateOf(false) }
    var confirmPwVisible by remember { mutableStateOf(false) }

    // Logic kiểm tra điều kiện
    val hasMinLength = newPassword.length >= 8
    val hasUpperAndNumber = newPassword.any { it.isUpperCase() } && newPassword.any { it.isDigit() }
    val hasSpecialChar = newPassword.any { !it.isLetterOrDigit() }

    val strength = listOf(hasMinLength, hasUpperAndNumber, hasSpecialChar).count { it }
    val isMatched = newPassword == confirmPassword && newPassword.isNotEmpty()
    val isFormValid = strength == 3 && isMatched && currentPassword.isNotEmpty()

    Column(modifier = Modifier.fillMaxWidth()) {
        // --- Mật khẩu hiện tại ---
        PasswordField(
            label = "Mật khẩu hiện tại",
            value = currentPassword,
            onValueChange = { currentPassword = it },
            isVisible = currentPwVisible,
            onVisibilityToggle = { currentPwVisible = !currentPwVisible }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Mật khẩu mới ---
        PasswordField(
            label = "Mật khẩu mới",
            value = newPassword,
            onValueChange = { newPassword = it },
            isVisible = newPwVisible,
            onVisibilityToggle = { newPwVisible = !newPwVisible },
            isHighlight = true
        )

        // --- Độ mạnh mật khẩu ---
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Độ mạnh mật khẩu", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            val barModifier = Modifier.weight(1f).height(4.dp)
            Box(modifier = barModifier.background(if (strength >= 1) Color(0xFF4CAF50) else Color(0xFFE0E0E0), RoundedCornerShape(2.dp)))
            Box(modifier = barModifier.background(if (strength >= 2) Color(0xFF4CAF50) else Color(0xFFE0E0E0), RoundedCornerShape(2.dp)))
            Box(modifier = barModifier.background(if (strength >= 3) Color(0xFFFFA000) else Color(0xFFE0E0E0), RoundedCornerShape(2.dp)))
            Box(modifier = barModifier.background(if (strength == 3 && isMatched) Color(0xFFFFA000) else Color(0xFFE0E0E0), RoundedCornerShape(2.dp)))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Xác nhận mật khẩu mới ---
        PasswordField(
            label = "Xác nhận mật khẩu mới",
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            isVisible = confirmPwVisible,
            onVisibilityToggle = { confirmPwVisible = !confirmPwVisible }
        )

        // --- Checklist điều kiện ---
        Spacer(modifier = Modifier.height(16.dp))
        ConditionRow("Ít nhất 8 ký tự", hasMinLength)
        Spacer(modifier = Modifier.height(8.dp))
        ConditionRow("Chứa chữ hoa và số", hasUpperAndNumber)
        Spacer(modifier = Modifier.height(8.dp))
        ConditionRow("Chứa ký tự đặc biệt", hasSpecialChar)

        Spacer(modifier = Modifier.height(32.dp))

        // --- Nút Lưu ---
        Button(
            onClick = { onSaveClick(currentPassword, newPassword) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid) Color(0xFF1E1E2C) else Color(0xFFF5F5F9),
                contentColor = if (isFormValid) Color.White else Color.Gray
            ),
            enabled = isFormValid
        ) {
            Text("Lưu mật khẩu", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isVisible: Boolean,
    onVisibilityToggle: () -> Unit,
    isHighlight: Boolean = false
) {
    Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isHighlight) Color(0xFFFFA000) else Color.DarkGray)
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onVisibilityToggle) {
                Icon(
                    imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = if (isHighlight) Color(0xFFFFA000) else Color.LightGray,
            focusedBorderColor = if (isHighlight) Color(0xFFFFA000) else Color(0xFF1E1E2C)
        )
    )
}

@Composable
fun ConditionRow(text: String, isMet: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (isMet) Icons.Default.CheckCircle else Icons.Default.Cancel,
            contentDescription = null,
            tint = if (isMet) Color(0xFF4CAF50) else Color.LightGray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (isMet) Color(0xFF4CAF50) else Color.Gray,
            fontWeight = if (isMet) FontWeight.Bold else FontWeight.Normal
        )
    }
}