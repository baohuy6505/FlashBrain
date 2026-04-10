package com.example.android.presentation.screens.profile.components.profile_content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.presentation.ui.theme.*

@Composable
fun ProStatusCard(
    isPro: Boolean,
    packageName: String, // Thêm tên gói (ví dụ: Premium Monthly)
    price: Double,       // Thêm giá gói (ví dụ: 9.99)
    expiryDate: String? = null
) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        color = CardWhite,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PRO STATUS",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextBlack
                )
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (isPro) PrimaryBlue else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Hiển thị tên gói động
            Text(
                text = if (isPro) packageName else "Basic Plan",
                fontSize = 18.sp, // Tăng nhẹ size cho nổi bật tên gói
                fontWeight = FontWeight.Bold,
                color = TextBlack
            )

            // Hiển thị ngày hết hạn và giá
            Text(
                text = if (isPro) "Expires: $expiryDate ($$price)" else "Upgrade to unlock all features",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextGray.copy(alpha = 0.8f)
            )

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (isPro) "Manage Plan" else "Upgrade Now",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}