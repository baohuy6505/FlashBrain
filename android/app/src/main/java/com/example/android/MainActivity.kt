package com.example.android

import android.content.Context
import android.os.Bundle
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
import com.example.android.presentation.layouts.MainLayout
import com.example.android.presentation.manager.StudyReminderManager
import com.example.android.presentation.screens.profile.ProfileScreen
import com.example.android.presentation.screens.profile.ProfileViewModel
import com.example.android.presentation.screens.study.StudyScreen
import com.example.android.presentation.screens.premium.PremiumScreen
import com.example.android.presentation.screens.auth.register.RegisterScreen
import com.example.android.presentation.screens.auth.forgot_password.ForgotPasswordScreen
import com.example.android.presentation.screens.flashcard.DecksScreen
import com.example.android.presentation.screens.splash.SplashScreen
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

                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {

                    // ── SPLASH ──
                    composable("splash") {
                        SplashScreen(onNavigateNext = {
                            navController.navigate("main_flow") {
                                popUpTo("splash") { inclusive = true }
                            }
                        })
                    }

                    // ── MAIN FLOW (Đồng bộ Drawer & Bottom Bar) ──
                    composable("main_flow") {
                        val profileViewModel: ProfileViewModel = hiltViewModel()
                        val userData by profileViewModel.userData.collectAsState()
                        var currentTab by remember { mutableIntStateOf(0) }

                        MainLayout(
                            currentTab = currentTab,
                            user = userData,
                            onTabSelected = { currentTab = it },
                            onNavigateToNotification = {
                                navController.navigate("notification")
                            },
                            onLogoutClick = {
                                navController.navigate("splash") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        ) {
                            when (currentTab) {
                                0 -> HomeScreen(
                                    onNavigateToDecks = { currentTab = 1 }
                                )
                                1 -> DecksScreen(
                                    onDeckClick = { deckId ->
                                        navController.navigate("flashcard_list/$deckId")
                                    }
                                )
                                2 -> PremiumScreen()
                                3 -> ProfileScreen(
                                    viewModel = profileViewModel,
                                    onNavigateToChangePassword = {
                                        navController.navigate("change_password")
                                    }
                                )
                            }
                        }
                    }

                    // ── FLASHCARD LIST ──
                    composable(
                        route = "flashcard_list/{deckId}",
                        arguments = listOf(
                            navArgument("deckId") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val deckId = backStackEntry.arguments?.getString("deckId").orEmpty()
                        FlashcardListScreen(
                            deckId = deckId,
                            onBack = { navController.popBackStack() },
                            onReviewClick = { navController.navigate("study/$deckId") }
                        )
                    }

                    // ── STUDY ──
                    composable(
                        route = "study/{deckId}",
                        arguments = listOf(
                            navArgument("deckId") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val deckId = backStackEntry.arguments?.getString("deckId").orEmpty()
                        StudyScreen(
                            deckId = deckId,
                            onExit = { navController.popBackStack() }
                        )
                    }

                    // ── NOTIFICATION ──
                    composable("notification") {
                        NotificationScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // ── CHANGE PASSWORD ──
                    composable("change_password") {
                        ChangePasswordScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // ── FORGOT PASSWORD ──
                    composable("forgot_password") {
                        ForgotPasswordScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToLogin = { navController.popBackStack() }
                        )
                    }

                    // ── REGISTER ──
                    composable("register") {
                        RegisterScreen(
                            onNavigateToLogin = { navController.popBackStack() },
                            onNavigateToHome = {
                                navController.navigate("main_flow") {
                                    popUpTo("register") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}