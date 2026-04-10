package com.example.android.presentation.screens.profile.components.profile_content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.presentation.ui.theme.ProGold
import com.example.android.presentation.ui.theme.StreakBg

@Composable
fun StreakCard(streakCount: Int) { // Quan trọng: Phải có tham số này
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        color = StreakBg
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "LEARNING STREAK",
                    fontSize = 12.sp,
                    color = ProGold,
                    fontWeight = FontWeight.SemiBold
                )

                // Hiển thị số ngày truyền vào từ ProfileScreen
                Text(
                    text = "$streakCount Days",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ProGold
                )

                Text(
                    text = "KEEP IT UP!",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ProGold.copy(alpha = 0.7f),
                    letterSpacing = 0.5.sp
                )
            }

            Icon(
                imageVector = Icons.Default.OfflineBolt,
                contentDescription = "Streak Icon",
                tint = ProGold,
                modifier = Modifier
                    .size(32.dp)
                    .padding(top = 2.dp)
            )
        }
    }
}

