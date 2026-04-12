package com.example.android.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.domain.model.User
import com.example.android.presentation.screens.profile.components.profile_content.*
import com.example.android.presentation.screens.profile.components.profile_setting.*
import com.example.android.presentation.ui.theme.BgGray
import com.example.android.presentation.ui.theme.PrimaryBlue

@Composable
fun ProfileScreen(
    onNavigateToChangePassword: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userData by viewModel.userData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Lắng nghe sự kiện thông báo - Dứt điểm lỗi lặp lại tại đây
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ProfileUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    // Nếu userData chưa load kịp thì dùng dữ liệu từ sessionManager hoặc mặc định
    val user = userData ?: User(
        id = "unknown",
        name = "Bảo Huy",
        email = "baohuy6505@gmail.com",
        subscriptionType = "PREMIUM",
        balance = 1240.50
    )

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(BgGray),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                HeaderUserSection(
                    name = user.name,
                    email = user.email,
                    balance = user.balance,
                    isPro = user.subscriptionType == "PREMIUM",
                    avatarUri = user.image,
                    onUpdateClick = { newName, imagePath ->
                        viewModel.updateProfile(newName, imagePath)
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                ProStatusCard(
                    isPro = user.subscriptionType == "PREMIUM",
                    packageName = "Premium",
                    price = 9.99,
                    expiryDate = "2026"
                )
            }

            item { SectionTitle(title = "GENERAL SETTINGS") }
            item { GeneralSettingsSection() }

            item { SectionTitle(title = "SECURITY & ACCOUNT") }
            item {
                AccountSettingsSection(
                    onChangePasswordClick = onNavigateToChangePassword,
                    onLogoutClick = { /* Logout */ }
                )
            }
        }

        // Overlay Loading khi đang cập nhật
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}