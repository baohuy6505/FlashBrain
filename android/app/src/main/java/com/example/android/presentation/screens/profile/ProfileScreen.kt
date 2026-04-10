package com.example.android.presentation.screens.profile

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.domain.model.ProPackage
import com.example.android.domain.model.User
import com.example.android.domain.model.UserProgress
// Import toàn bộ components đã tách ra (Header, Card, Sections, Rows)
import com.example.android.presentation.screens.profile.components.profile_content.HeaderUserSection
import com.example.android.presentation.screens.profile.components.profile_content.ProStatusCard
import com.example.android.presentation.screens.profile.components.profile_content.SectionTitle
import com.example.android.presentation.screens.profile.components.profile_content.StreakCard
import com.example.android.presentation.screens.profile.components.profile_setting.*
import com.example.android.presentation.ui.theme.BgGray

@Composable
fun ProfileScreen(
    onNavigateToChangePassword: () -> Unit
) {
    // 1. DỮ LIỆU GỐC (Trong thực tế sẽ lấy từ ViewModel/Repository)
    val fakeUser = remember {
        User(
            id = "1",
            name = "Xuan Trung",
            email = "phanxuantrungpxt123@gmail.com",
            subscriptionType = "PREMIUM",
            balance = 1240.50
        )
    }

    val currentPackage = remember {
        ProPackage(
            id = "pkg_001",
            name = "Premium Monthly",
            price = 9.99,
            durationDays = 30
        )
    }

    val userProgress = remember {
        UserProgress(userId = "1", streakDays = 12)
    }

    // 2. QUẢN LÝ TRẠNG THÁI (UI STATE)
    // Các biến này lưu giữ thay đổi tạm thời trên màn hình
    var currentUserName by remember { mutableStateOf(fakeUser.name) }
    var currentAvatarUri by remember { mutableStateOf<Uri?>(null) }

    // 3. GIAO DIỆN CHÍNH
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgGray),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // --- Phần Header: Avatar, Tên, Số dư ---
        item {
            HeaderUserSection(
                name = currentUserName,
                email = fakeUser.email,
                balance = fakeUser.balance,
                isPro = fakeUser.subscriptionType == "PREMIUM",
                avatarUri = currentAvatarUri,
                onNameChange = { newName ->
                    currentUserName = newName
                },
                onAvatarChange = { newUri ->
                    currentAvatarUri = newUri
                }
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        // --- Thẻ trạng thái Hội viên ---
        item {
            ProStatusCard(
                isPro = fakeUser.subscriptionType == "PREMIUM",
                packageName = currentPackage.name,
                price = currentPackage.price,
                expiryDate = "April 12, 2026"
            )
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // --- Thẻ học tập ---
        item {
            StreakCard(streakCount = userProgress.streakDays)
        }

        // --- Nhóm Cài đặt chung (General) ---
        // SectionTitle hiện đã nằm ở file riêng trong folder components
        item { SectionTitle(title = "GENERAL SETTINGS") }
        item { GeneralSettingsSection() }

        // --- Nhóm Tài khoản (Security) ---
        item { SectionTitle(title = "SECURITY & ACCOUNT") }
        item {
            AccountSettingsSection(
                onChangePasswordClick = onNavigateToChangePassword,
                onLogoutClick = {
                    // Xử lý đăng xuất tại đây
                }
            )
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}