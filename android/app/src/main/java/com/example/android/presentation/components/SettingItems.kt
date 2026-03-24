package com.example.android.presentation.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.android.presentation.ui.theme.*

@Composable
fun SettingRowBase(icon: ImageVector, title: String, subtitle: String, isDestructive: Boolean = false, content: @Composable RowScope.() -> Unit = {}) {
    val iconBgColor = if (isDestructive) RedLogout.copy(alpha = 0.1f) else SettingIconDark.copy(alpha = 0.4f)
    val iconTint = if (isDestructive) RedLogout else SystemIconGray

    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(iconBgColor), contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = if (isDestructive) RedLogout else TextBlack)
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
fun SettingRowValue(
    icon: ImageVector,
    title: String,
    subtitle: String,
    value: String,
    hasNavIcon: Boolean = false
) {
    SettingRowBase(icon = icon, title = title, subtitle = subtitle) {
        // Tạo một cái nền bo góc bao quanh cả chữ và mũi tên
        Surface(
            color = BgGray, // Màu nền xám nhạt
            shape = RoundedCornerShape(12.dp), // Bo góc cho cái tag
            modifier = Modifier.wrapContentWidth() // Tự động dài theo nội dung
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp) // Tạo độ dày cho tag
            ) {
                Text(
                    text = value,
                    color = TextGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                if (hasNavIcon) {
                    Spacer(modifier = Modifier.width(4.dp)) // Khoảng cách giữa chữ và mũi tên
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = TextGray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SettingRowClickable(icon: ImageVector, title: String, subtitle: String, isDestructive: Boolean = false) {
    SettingRowBase(icon = icon, title = title, subtitle = subtitle, isDestructive = isDestructive) {
        if (!isDestructive) Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = TextGray)
    }
}