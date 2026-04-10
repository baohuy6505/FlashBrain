package com.example.android.presentation.screens.study



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.presentation.ui.theme.*

@Composable
fun StudyHeader(current: Int, total: Int, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.Close, contentDescription = "Exit", modifier = Modifier.size(28.dp), tint = TextBlack)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Advanced Cognitive Psychology",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextBlack,
                maxLines = 1
            )
            Text(
                text = "EXHIBIT $current OF $total",
                fontSize = 11.sp,
                color = TextGray,
                fontWeight = FontWeight.Bold
            )
        }

        LinearProgressIndicator(
            progress = { current.toFloat() / total.toFloat() },
            modifier = Modifier
                .width(70.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(10.dp)),
            color = PrimaryBlue,
            trackColor = BgGray
        )
    }
}