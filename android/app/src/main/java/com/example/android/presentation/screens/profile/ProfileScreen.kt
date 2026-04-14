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

    // Dùng biến này để biết đã bấm logout chưa, tránh hiện Loading vô tận
    var isLoggingOut by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ProfileUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is ProfileUiEvent.LogoutSuccess -> {
                    isLoggingOut = true // Đánh dấu là đang chuyển màn hình
                    onLogoutSuccess()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // CHỈ hiện vòng xoay trung tâm nếu không phải đang trong quá trình logout và data chưa về
        if (userData == null && !isLoggingOut) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        }

        // Hiển thị nội dung nếu có data
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
                        packageName = "Premium Plan",
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
                        onLogoutClick = {
                            viewModel.logout()
                        }
                    )
                }
            }
        }

        // Loading Overlay (Vòng xoay đè lên khi đang gọi API)
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