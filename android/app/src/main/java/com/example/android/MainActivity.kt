package com.example.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.presentation.layouts.MainLayout
import com.example.android.presentation.screens.auth.login.LoginScreen
import com.example.android.presentation.screens.profile.ProfileScreen
import com.example.android.presentation.screens.study.StudyScreen
import com.example.android.presentation.screens.premium.PremiumScreen
import com.example.android.presentation.screens.auth.register.RegisterScreen
import com.example.android.presentation.screens.auth.forgot_password.ForgotPasswordScreen
import com.example.android.presentation.screens.splash.SplashScreen
import com.example.android.presentation.screens.notification.NotificationScreen
import com.example.android.presentation.screens.profile.change_password.ChangePasswordScreen
import com.example.android.presentation.screens.desk.DecksScreen
import com.example.android.presentation.screens.home.HomeScreen

import com.example.android.presentation.screens.listdesk.FlashcardListScreen
import com.example.android.presentation.ui.theme.AndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidTheme(dynamicColor = false) {
                // 1. Biến quản lý trạng thái hiển thị Splash Screen
                var isShowSplash by remember { mutableStateOf(true) }

                // Quản lý Tab hiện tại
                var currentTab by remember { mutableIntStateOf(3) }

                // KIỂM TRA: Mở app lên thì hiện Splash trước
                if (isShowSplash) {
                    SplashScreen(
                        onNavigateNext = {
                            // Sau 2.5 giây, tự động tắt Splash
                            isShowSplash = false
                            // Chuyển tới tab 0 (Màn hình Đăng nhập)
                            currentTab = 0
                        }
                    )
                }
                else {
                    // NHÓM 1: CÁC MÀN HÌNH NẰM TRONG MAIN LAYOUT (Có Bottom Bar)
                    if (currentTab == 0 || currentTab == 1 || currentTab == 2 || currentTab == 3) {
                        MainLayout(
                            currentTab = currentTab,
                            onTabSelected = { currentTab = it },
                            onNavigateToNotification = {
                                currentTab = 6 // Chuyển sang màn hình Notification
                            },
                            onLogoutClick = {
                                currentTab = 0 // Đăng xuất thì về Login
                            }
                        ) {
                            when (currentTab) {
//                                0 -> LoginScreen(
//                                    onNavigateToSignUp = { currentTab = 5 }, // Sang Đăng ký
//                                    onNavigateToHome = { currentTab = 3 },   // Sang Profile
//                                    onNavigateToForgotPassword = { currentTab = 4 } // Sang Quên MK
//                                )
                                0 -> HomeScreen(
                                    onNavigateToDecks = {
                                        currentTab = 1
                                    }
                                )
                                1 -> DecksScreen(
                                    onDeckClick = { deckId ->
                                        // TODO: Có thể lưu deckId vào state nếu muốn truyền dữ liệu thật
                                        currentTab = 8 // Chuyển sang màn hình chi tiết bộ thẻ (FlashcardList)
                                    }
                                )
                                2 -> PremiumScreen()
                                3 -> ProfileScreen(
                                    onNavigateToChangePassword = { currentTab = 7 }
                                )
                            }
                        }
                    }
                    // NHÓM 2: CÁC MÀN HÌNH ĐỘC LẬP (Không có Bottom Bar)
                    else {
                        when (currentTab) {
                            4 -> ForgotPasswordScreen(
                                onNavigateBack = { currentTab = 0 }, // Back về Login
                                onNavigateToLogin = { currentTab = 0 } // Bấm Đăng nhập về Login
                            )
                            5 -> RegisterScreen(
                                onNavigateToLogin = { currentTab = 0 },
                                onNavigateToHome = { currentTab = 3 }
                            )
                            6 -> NotificationScreen(
                                onNavigateBack = { currentTab = 3 } // Back về Settings (Profile)
                            )
                            7 -> ChangePasswordScreen(
                                onNavigateBack = { currentTab = 3 } // Xong xuôi quay lại tab Profile
                            )
                            // CHI TIẾT BỘ THẺ (Tab 8)
                            8 -> FlashcardListScreen(
                                onBack = { currentTab = 1 }, // Bấm Back quay lại danh sách Decks
                                onReviewClick = { currentTab = 9 } // Bấm "Review Now" thì vào màn hình học (Study)
                            )
                            // MÀN HÌNH HỌC TẬP (Tab 9)
                            9 -> StudyScreen(
                                onExit = { currentTab = 8 } // Bấm nút tắt (X) thì quay lại trang chi tiết bộ thẻ
                            )
                        }
                    }
                }
            }
        }
    }
}