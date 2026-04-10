package com.example.android.presentation.screens.profile.components.profile_setting



import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.presentation.ui.theme.BgGray
import com.example.android.presentation.ui.theme.CardWhite

@Composable
fun GeneralSettingsSection() {
    Surface(
        modifier = Modifier.padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        color = CardWhite
    ) {
        Column {
            var noti by remember { mutableStateOf(true) }
            SettingRowSwitch(Icons.Default.Notifications, "Push Notifications", "Daily reminders", noti) { noti = it }
            HorizontalDivider(modifier = Modifier.padding(start = 72.dp), color = BgGray.copy(alpha = 0.5f))
            SettingRowValue(Icons.Default.DarkMode, "Appearance", "Match system", "System")
            HorizontalDivider(modifier = Modifier.padding(start = 72.dp), color = BgGray.copy(alpha = 0.5f))
            SettingRowValue(Icons.Default.Language, "Language", "App interface", "English", true)
        }
    }
}

@Composable
fun AccountSettingsSection(
    onChangePasswordClick: () -> Unit,
    onLogoutClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        color = CardWhite
    ) {
        Column {
            SettingRowClickable(
                icon = Icons.Default.Lock,
                title = "Change Password",
                subtitle = "Update credentials",
                onClick = onChangePasswordClick
            )
            HorizontalDivider(modifier = Modifier.padding(start = 72.dp), color = BgGray.copy(alpha = 0.5f))
            SettingRowClickable(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                title = "Logout",
                subtitle = "Sign out",
                isWarning = true,
                onClick = onLogoutClick
            )
        }
    }
}