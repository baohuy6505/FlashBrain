package com.example.android.presentation.screens.auth.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
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
            RegisterHeader()

            Spacer(modifier = Modifier.height(32.dp))

            RegisterForm(
                onRegisterClick = { name, email, password ->
                    // Logic đăng ký bằng model User sẽ đặt ở ViewModel
                    onNavigateToHome()
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            RegisterSocialSection(
                onGoogleRegisterClick = { },
                onLoginClick = onNavigateToLogin
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}