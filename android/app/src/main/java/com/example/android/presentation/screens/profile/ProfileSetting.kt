package com.example.android.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.presentation.ui.theme.*

// --- CÁC THÀNH PHẦN CƠ BẢN (REUSABLE COMPONENTS) ---

@Composable
fun SettingRowBase(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isWarning: Boolean = false, // Đã đổi tên từ isDestructive
    content: @Composable RowScope.() -> Unit = {}
) {
    val iconBgColor = if (isWarning) RedLogout.copy(alpha = 0.1f) else SettingIconDark.copy(alpha = 0.4f)
    val iconTint = if (isWarning) RedLogout else SystemIconGray

    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = if (isWarning) RedLogout else TextBlack)
            Text(text = subtitle, fontSize = 12.sp, color = TextGray)
        }
        content()
    }
}

@Composable
fun SettingRowSwitch(icon: ImageVector, title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    SettingRowBase(icon = icon, title = title, subtitle = subtitle) {
        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedTrackColor = PrimaryBlue))
    }
}

@Composable
fun SettingRowValue(icon: ImageVector, title: String, subtitle: String, value: String, hasNavIcon: Boolean = false) {
    SettingRowBase(icon = icon, title = title, subtitle = subtitle) {
        Surface(color = BgGray, shape = RoundedCornerShape(12.dp), modifier = Modifier.wrapContentWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                Text(text = value, color = TextGray, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                if (hasNavIcon) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = TextGray, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun SettingRowClickable(icon: ImageVector, title: String, subtitle: String, isWarning: Boolean = false) {
    SettingRowBase(icon = icon, title = title, subtitle = subtitle, isWarning = isWarning) {
        if (!isWarning) Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = TextGray)
    }
}

// --- CÁC PHẦN CHÍNH (SECTIONS) DÙNG TRONG SCREEN ---

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
fun AccountSettingsSection() {
    Surface(
        modifier = Modifier.padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        color = CardWhite
    ) {
        Column {
            SettingRowClickable(Icons.Default.Lock, "Change Password", "Update credentials")
            HorizontalDivider(modifier = Modifier.padding(start = 72.dp), color = BgGray.copy(alpha = 0.5f))
            // Chỗ này truyền isWarning = true để nút Logout hiện màu đỏ
            SettingRowClickable(Icons.AutoMirrored.Filled.ExitToApp, "Logout", "Sign out", isWarning = true)
        }
    }
}