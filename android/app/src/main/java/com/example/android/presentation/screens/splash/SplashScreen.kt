package com.example.android.presentation.screens.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateNext: () -> Unit) {
    // Tự động chuyển trang sau 2.5 giây
    LaunchedEffect(key1 = true) {
        delay(2500L)
        onNavigateNext()
    }

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SplashLogo()

            Spacer(modifier = Modifier.height(56.dp))

            SplashText()

            Spacer(modifier = Modifier.height(80.dp))

            SplashIndicator()
        }
    }
}