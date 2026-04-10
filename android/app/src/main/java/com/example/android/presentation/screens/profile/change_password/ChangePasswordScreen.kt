package com.example.android.presentation.screens.profile.change_password

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChangePasswordScreen(onNavigateBack: () -> Unit) {
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
            ChangePasswordHeader(onBackClick = onNavigateBack)

            Spacer(modifier = Modifier.height(32.dp))

            ChangePasswordForm(
                onSaveClick = { currentPw, newPw ->
                    // Xử lý logic gọi API đổi mật khẩu ở đây
                    onNavigateBack()
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}