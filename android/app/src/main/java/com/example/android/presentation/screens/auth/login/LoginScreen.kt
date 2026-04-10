package com.example.android.presentation.screens.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToForgotPassword: () -> Unit // THÊM DÒNG NÀY
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
            LoginHeader()

            Spacer(modifier = Modifier.height(32.dp))

            LoginForm(
                onLoginClick = { email, password ->
                    onNavigateToHome()
                },
                onForgotPasswordClick = onNavigateToForgotPassword // GỌI Ở ĐÂY
            )

            Spacer(modifier = Modifier.height(32.dp))

            SocialLoginSection(
                onGoogleLoginClick = { },
                onSignUpClick = onNavigateToSignUp
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}