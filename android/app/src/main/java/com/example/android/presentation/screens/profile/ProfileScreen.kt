package com.example.android.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// Đảm bảo import đúng các component Profile và các Section Setting mới
import com.example.android.presentation.screens.profile.components.*
import com.example.android.presentation.ui.theme.BgGray

@Composable
fun ProfileScreen(
    onNavigateToChangePassword: () -> Unit // Thêm param này
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgGray),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // --- Nhóm Thông tin cá nhân ---
        item { HeaderUserSection() }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item { ProStatusCard() }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { StreakCard() }

        // --- Nhóm Cài đặt chung ---
        item { SectionTitle("GENERAL SETTINGS") }
        item { GeneralSettingsSection() } // Hàm này gọi từ file Setting bạn vừa tạo

        // --- Nhóm Tài khoản ---
        item { SectionTitle("SECURITY & ACCOUNT") }
        item { AccountSettingsSection(
            onChangePasswordClick = onNavigateToChangePassword)
        } // Hàm này gọi từ file Setting bạn vừa tạo

        // Khoảng nghỉ cuối danh sách
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}