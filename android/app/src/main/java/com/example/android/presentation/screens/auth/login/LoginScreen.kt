package com.example.android.presentation.screens.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Xử lý các sự kiện (Event) từ ViewModel trả về UI
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
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
            // 1. Header: Logo hoặc Tiêu đề chào mừng
            LoginHeader()

            Spacer(modifier = Modifier.height(32.dp))

            // 2. Form: Email, Password và nút Login
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

            // 3. Social: Nút Google và dòng chuyển sang Đăng ký
            SocialLoginSection(
                onGoogleLoginClick = {
                    // Kích hoạt luồng đăng nhập Google
                    scope.launch {
                        // Gọi hàm xử lý trong ViewModel.
                        // ViewModel sẽ phối hợp với Credential Manager để lấy idToken
                        viewModel.onGoogleLoginClick(context)
                    }
                },
                onSignUpClick = onNavigateToRegister
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}