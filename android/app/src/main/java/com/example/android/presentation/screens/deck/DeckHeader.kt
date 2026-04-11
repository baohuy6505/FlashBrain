package com.example.android.presentation.screens.desk

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DeckHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .offset(y = (-20).dp) // Nhích lên nhẹ nhàng hơn
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Điểm nhấn: Một chấm tròn màu Gradient trước tiêu đề
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFAB47BC), Color(0xFFE91E63))
                        ),
                        shape = CircleShape
                    )
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Tiêu đề với màu sắc đậm nét hơn (Deep Blue)
            Text(
                text = "My Decks",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp,
                color = Color(0xFF1A237E)
            )
        }

        // Dòng giới thiệu được căn chỉnh lại
        Text(
            text = "Manage your cognitive collections and expand your knowledge.",
            fontSize = 15.sp,
            color = Color(0xFF78909C), // Màu xám xanh cho hiện đại
            lineHeight = 22.sp,
            modifier = Modifier
                .padding(top = 4.dp, start = 22.dp) // Thụt đầu dòng để thẳng hàng với text tiêu đề
                .fillMaxWidth(0.8f) // Giới hạn chiều rộng để chữ tự xuống dòng nhìn chuyên nghiệp hơn
        )

        // Đường kẻ gạch chân mờ ảo trang trí
        Box(
            modifier = Modifier
                .padding(top = 16.dp, start = 22.dp)
                .width(40.dp)
                .height(4.dp)
                .background(
                    color = Color(0xFFE1F5FE),
                    shape = CircleShape
                )
        )
    }
}