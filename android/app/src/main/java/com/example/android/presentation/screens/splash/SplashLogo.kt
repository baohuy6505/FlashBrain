package com.example.android.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SplashLogo() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LogoBox(
                bgColor = Color(0xFFFFC107),
                icon = Icons.Default.Dashboard,
                iconTint = Color(0xFF1E1E2C)
            )
            LogoBox(
                bgColor = Color(0xFF283593),
                icon = Icons.Default.Star,
                iconTint = Color(0xFFFFC107)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LogoBox(
                bgColor = Color(0xFF283593),
                icon = Icons.Default.Schedule,
                iconTint = Color(0xFF4FC3F7)
            )
            LogoBox(
                bgColor = Color(0xFF3F51B5),
                icon = Icons.Default.TrendingUp,
                iconTint = Color.White
            )
        }
    }
}

@Composable
fun LogoBox(bgColor: Color, icon: ImageVector, iconTint: Color) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(36.dp)
        )
    }
}