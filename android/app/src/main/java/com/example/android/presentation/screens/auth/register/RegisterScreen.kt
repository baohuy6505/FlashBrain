package com.example.android.presentation.screens.auth.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel() // Inject ViewModel ở đây
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Lắng nghe sự kiện từ ViewModel để điều hướng hoặc báo lỗi
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is RegisterUiEvent.Success -> {
                    // Đăng ký xong chuyển thẳng vào Home vì UserRepository đã lưu session rồi
                    onNavigateToLogin()
                }
                is RegisterUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) } // Cần có để hiện lỗi
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

            // Gắn dữ liệu và logic từ ViewModel vào Form
            RegisterForm(
                name = viewModel.name,
                email = viewModel.email,
                password = viewModel.password,
                onNameChange = { viewModel.name = it },
                onEmailChange = { viewModel.email = it },
                onPasswordChange = { viewModel.password = it },
                onRegisterClick = { viewModel.onRegisterClick() },
                isLoading = viewModel.isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

            RegisterSocialSection(
                onGoogleRegisterClick = { /* TODO */ },
                onLoginClick = onNavigateToLogin
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}