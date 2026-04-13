package com.example.android.presentation.screens.profile

import android.util.Log
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
import com.example.android.presentation.screens.profile.components.profile_content.*
import com.example.android.presentation.screens.profile.components.profile_setting.*
import com.example.android.presentation.ui.theme.BgGray
import com.example.android.presentation.ui.theme.PrimaryBlue

@Composable
fun ProfileScreen(
    onNavigateToChangePassword: () -> Unit,
    onLogoutSuccess: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userData by viewModel.userData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Lắng nghe sự kiện từ ViewModel
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ProfileUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is ProfileUiEvent.LogoutSuccess -> {
                    Log.d("PROFILE_DEBUG", "3. Đã nhận LogoutSuccess -> Điều hướng về Login")
                    onLogoutSuccess()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        userData?.let { user ->
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
                        packageName = "Premium Plan", // THÊM DÒNG NÀY
                        price = 9.99,                 // Đảm bảo truyền đủ các tham số bắt buộc khác
                        expiryDate = "2026"
                    )
                }

                item { SectionTitle(title = "GENERAL SETTINGS") }
                item { GeneralSettingsSection() }

                item { SectionTitle(title = "SECURITY & ACCOUNT") }
                item {
                    // QUAN TRỌNG: Kiểm tra truyền onLogoutClick
                    AccountSettingsSection(
                        onChangePasswordClick = onNavigateToChangePassword,
                        onLogoutClick = {
                            Log.d("PROFILE_DEBUG", "1. Click vào nút Logout trên UI")
                            viewModel.logout()
                        }
                    )
                }
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        }

        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}