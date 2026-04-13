package com.example.android.presentation.screens.profile.components.profile_setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.presentation.ui.theme.*

@Composable
fun SettingRowBase(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isWarning: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit = {}
) {
    val iconBgColor = if (isWarning) RedLogout.copy(alpha = 0.1f) else SettingIconDark.copy(alpha = 0.4f)
    val iconTint = if (isWarning) RedLogout else SystemIconGray

    Row(
        modifier = modifier.fillMaxWidth().padding(16.dp),
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
fun SettingRowClickable(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isWarning: Boolean = false,
    onClick: () -> Unit
) {
    SettingRowBase(
        icon = icon,
        title = title,
        subtitle = subtitle,
        isWarning = isWarning,
        modifier = Modifier.clickable { onClick() }
    ) {
        if (!isWarning) Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = TextGray)
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = value, color = TextGray, fontSize = 14.sp)
            if (hasNavIcon) Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = TextGray)
        }
    }
}