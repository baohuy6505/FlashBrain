package com.example.android.presentation.screens.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.R
import com.example.android.presentation.ui.theme.*

@Composable
fun HeaderUserSection() {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        color = CardWhite,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.size(110.dp)
            ) {
                // Avatar với viền vàng Pro
                Box(
                    modifier = Modifier
                        .size(108.dp)
                        .border(3.dp, ProGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avt),
                        contentDescription = "User Avatar",
                        modifier = Modifier.size(100.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                // Nhãn PRO
                Surface(
                    color = ProGold,
                    shape = CircleShape,
                    shadowElevation = 4.dp,
                    modifier = Modifier.offset(x = 6.dp, y = 6.dp)
                ) {
                    Text(
                        text = "PRO",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Xuan Trung", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = TextBlack)
            Text(text = "phanxuantrungpxt123@gmail.com", fontSize = 14.sp, color = TextGray)

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = BgGray, thickness = 2.dp)
            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "TOTAL BALANCE", fontSize = 12.sp, color = TextGray, fontWeight = FontWeight.SemiBold)
            Text(text = "$1,240.50", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
        }
    }
}

@Composable
fun ProStatusCard() {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        color = CardWhite,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "PRO STATUS", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextBlack)
                Icon(Icons.Default.CheckCircle, null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Pro Membership Active", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextBlack)
            Text(text = "Ngày 12 tháng 03 năm 2005", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextGray.copy(alpha = 0.8f))
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Manage Plan", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun StreakCard() {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        color = StreakBg
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "LEARNING STREAK", fontSize = 12.sp, color = ProGold, fontWeight = FontWeight.SemiBold)
                Text(text = "12 Days", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = ProGold)
                Text(text = "KEEP IT UP, ALEX!", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ProGold.copy(alpha = 0.7f), letterSpacing = 0.5.sp)
            }
            Icon(Icons.Default.OfflineBolt, null, tint = ProGold, modifier = Modifier.size(32.dp).padding(top = 2.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = TextGray,
        modifier = Modifier.padding(start = 24.dp, bottom = 8.dp, top = 16.dp)
    )
}