package com.example.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp // <-- THÊM DÒNG NÀY ĐỂ HẾT LỖI 'dp'
import com.example.android.presentation.components.CommonBottomNavigation
import com.example.android.presentation.components.CommonTopBar
import com.example.android.presentation.screens.profile.ProfileScreen
import com.example.android.presentation.ui.theme.AndroidTheme
import com.example.android.presentation.ui.theme.BgGray

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidTheme(dynamicColor = false) {
                // Quản lý Tab hiện tại (0: Gallery, 1: Decks, 2: Premium, 3: Settings)
                var currentTab by remember { mutableIntStateOf(3) }

                Scaffold(
                    topBar = { CommonTopBar() },
                    bottomBar = {
                        CommonBottomNavigation(selectedItem = currentTab) { currentTab = it }
                    },
                    containerColor = BgGray
                ) { innerPadding ->
                    // innerPadding giúp nội dung không bị đè bởi TopBar và BottomBar
                    Box(modifier = Modifier.padding(innerPadding)) {
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
}