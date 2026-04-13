package com.example.android.presentation.screens.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Xử lý sự kiện trả về từ ViewModel
    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event: LoginUiEvent -> // Chỉ định kiểu rõ ràng để hết lỗi "Cannot infer type"
            when (event) {
                is LoginUiEvent.Success -> {
                    onNavigateToHome()
                }
                is LoginUiEvent.Error -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) }
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

            // Kết nối State và Action từ ViewModel xuống Form
            LoginForm(
                email = viewModel.email,
                onEmailChange = { viewModel.email = it },
                password = viewModel.password,
                onPasswordChange = { viewModel.password = it },
                onLoginClick = { viewModel.onLoginClick() },
                onForgotPasswordClick = onNavigateToForgotPassword,
                isLoading = viewModel.isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

            SocialLoginSection(
                onGoogleLoginClick = { },
                onSignUpClick = onNavigateToRegister
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}