package com.example.android

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.android.data.worker.NotificationWorker
import com.example.android.domain.repository.NotificationRepository
import com.example.android.data.local.SessionManager
import com.example.android.presentation.layouts.MainLayout
import com.example.android.presentation.manager.StudyReminderManager
import com.example.android.presentation.screens.profile.ProfileScreen
import com.example.android.presentation.screens.profile.ProfileViewModel
import com.example.android.presentation.screens.profile.ProfileUiEvent
import com.example.android.presentation.screens.study.StudyScreen
import com.example.android.presentation.screens.premium.PremiumScreen
import com.example.android.presentation.screens.auth.register.RegisterScreen
import com.example.android.presentation.screens.auth.login.LoginScreen
import com.example.android.presentation.screens.auth.forgot_password.ForgotPasswordScreen
import com.example.android.presentation.screens.flashcard.DecksScreen
import com.example.android.presentation.screens.notification.NotificationScreen
import com.example.android.presentation.screens.profile.change_password.ChangePasswordScreen
import com.example.android.presentation.screens.home.HomeScreen
import com.example.android.presentation.screens.flashcard.FlashcardListScreen
import com.example.android.presentation.ui.theme.AndroidTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var notificationRepository: NotificationRepository

    // Inject SessionManager để kiểm tra trạng thái đăng nhập
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //treo thong bao chay ngam
        StudyReminderManager.startDailyReminders(this)

        //su dung de Demo chuc nang thong bao + NotificationWorker
//        val testRequest = androidx.work.OneTimeWorkRequestBuilder<NotificationWorker>().build()
//        androidx.work.WorkManager.getInstance(this).enqueue(testRequest)


        enableEdgeToEdge()

        setContent {
            AndroidTheme(dynamicColor = false) {
                val navController = rememberNavController()

                // Kiểm tra xem đã có Token trong máy chưa
                val isLoggedIn = remember { !sessionManager.authToken.isNullOrBlank() }

                NavHost(
                    navController = navController,
                    // Nếu đã logged in thì vào thẳng main_flow, ngược lại vào login
                    startDestination = if (isLoggedIn) "main_flow" else "login"
                ) {
                    // ── LOGIN ──
                    composable("login") {
                        LoginScreen(
                            onNavigateToRegister = { navController.navigate("register") },
                            onNavigateToHome = {
                                navController.navigate("main_flow") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToForgotPassword = { navController.navigate("forgot_password") }
                        )
                    }

                    // ── REGISTER ──
                    composable("register") {
                        RegisterScreen(
                            onNavigateToLogin = { navController.popBackStack() },
                            onNavigateToHome = {
                                navController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
                                }
                            }
                        )
                    }

                    // ── MAIN FLOW (App chính) ──
                    composable("main_flow") {
                        val profileViewModel: ProfileViewModel = hiltViewModel()
                        val userData by profileViewModel.userData.collectAsState()
                        var currentTab by remember { mutableIntStateOf(0) }

                        val navigateToAuth = {
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }

                        // Lắng nghe sự kiện Logout thành công
                        LaunchedEffect(Unit) {
                            profileViewModel.uiEvent.collect { event ->
                                if (event is ProfileUiEvent.LogoutSuccess) {
                                    navigateToAuth()
                                }
                            }
                        }

                        MainLayout(
                            currentTab = currentTab,
                            user = userData,
                            onTabSelected = { currentTab = it },
                            onNavigateToNotification = { navController.navigate("notification") },
                            onLogoutClick = {
                                Log.d("PROFILE_DEBUG", "Logout từ Sidebar")
                                profileViewModel.logout()
                            }
                        ) {
                            when (currentTab) {
                                0 -> HomeScreen(onNavigateToDecks = { currentTab = 1 })
                                1 -> DecksScreen(onDeckClick = { deckId ->
                                    navController.navigate("flashcard_list/$deckId")
                                })
                                2 -> PremiumScreen()
                                3 -> ProfileScreen(
                                    viewModel = profileViewModel,
                                    onNavigateToChangePassword = { navController.navigate("change_password") },
                                    onLogoutSuccess = { /* Đã có LaunchedEffect lo */ }
                                )
                            }
                        }
                    }

                    // ── ROUTES PHỤ ──
                    composable("notification") {
                        NotificationScreen(onNavigateBack = { navController.popBackStack() })
                    }

                    composable("change_password") {
                        ChangePasswordScreen(onNavigateBack = { navController.popBackStack() })
                    }

                    composable("forgot_password") {
                        ForgotPasswordScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToLogin = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = "flashcard_list/{deckId}",
                        arguments = listOf(navArgument("deckId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val deckId = backStackEntry.arguments?.getString("deckId").orEmpty()
                        FlashcardListScreen(
                            deckId = deckId,
                            onBack = { navController.popBackStack() },
                            onReviewClick = { navController.navigate("study/$deckId") }
                        )
                    }

                    composable(
                        route = "study/{deckId}",
                        arguments = listOf(navArgument("deckId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val deckId = backStackEntry.arguments?.getString("deckId").orEmpty()
                        StudyScreen(deckId = deckId, onExit = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}