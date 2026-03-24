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
import com.example.android.presentation.screens.profile.ProfileScreen
import com.example.android.presentation.ui.theme.AndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidTheme(dynamicColor = false) {
                // Quản lý Tab hiện tại (0: Gallery, 1: Decks, 2: Premium, 3: Settings/Profile)
                var currentTab by remember { mutableIntStateOf(3) }

                // Gọi MainLayout để bọc toàn bộ các màn hình
                MainLayout(
                    currentTab = currentTab,
                    onTabSelected = { currentTab = it }
                ) {
                    // Nội dung bên trong này sẽ tự động có Scaffold bao quanh
                    when (currentTab) {
                        0 -> Box(modifier = Modifier.padding(16.dp)) { Text("Gallery Screen") }
                        1 -> Box(modifier = Modifier.padding(16.dp)) { Text("Decks Screen") }
                        2 -> Box(modifier = Modifier.padding(16.dp)) { Text("Premium Screen") }
                        3 -> ProfileScreen()
                    }
                }
            }
        }
    }
}