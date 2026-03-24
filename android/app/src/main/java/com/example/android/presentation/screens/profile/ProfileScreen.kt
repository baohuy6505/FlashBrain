package com.example.android.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.presentation.components.*
import com.example.android.presentation.screens.profile.components.*
import com.example.android.presentation.ui.theme.*

@Composable
fun ProfileScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(BgGray),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item { HeaderUserSection() }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item { ProStatusCard() }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { StreakCard() }

        item { SectionTitle("GENERAL SETTINGS") }
        item {
            Surface(modifier = Modifier.padding(horizontal = 16.dp), shape = RoundedCornerShape(24.dp), color = CardWhite) {
                Column {
                    var noti by remember { mutableStateOf(true) }
                    SettingRowSwitch(Icons.Default.Notifications, "Push Notifications", "Daily reminders", noti) { noti = it }
                    HorizontalDivider(modifier = Modifier.padding(start = 72.dp), color = BgGray)
                    SettingRowValue(Icons.Default.DarkMode, "Appearance", "Match system", "System")
                    HorizontalDivider(modifier = Modifier.padding(start = 72.dp), color = BgGray)
                    SettingRowValue(Icons.Default.Language, "Language", "App interface", "English", true)
                }
            }
        }

        item { SectionTitle("SECURITY & ACCOUNT") }
        item {
            Surface(modifier = Modifier.padding(horizontal = 16.dp), shape = RoundedCornerShape(24.dp), color = CardWhite) {
                Column {
                    SettingRowClickable(Icons.Default.Lock, "Change Password", "Update credentials")
                    HorizontalDivider(modifier = Modifier.padding(start = 72.dp), color = BgGray)
                    SettingRowClickable(Icons.AutoMirrored.Filled.ExitToApp, "Logout", "Sign out", true)
                }
            }
        }
    }
}