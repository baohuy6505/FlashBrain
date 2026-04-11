package com.example.android.presentation.screens.listdesk

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.domain.model.Deck
import com.example.android.presentation.ui.theme.*

@Composable
fun FlashcardListHeader(
    deck: Deck,
    totalCards: Int,
    mastery: Int,
    onReviewClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
        Button(
            onClick = onReviewClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BlueMain)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = White)
            Spacer(Modifier.width(8.dp))
            @Suppress("DEPRECATION")
            Text("Review Now", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = White)
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("TOTAL CARDS", fontSize = 10.sp, color = TextLight)
                Text("$totalCards Cards", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
            }
            Box(modifier = Modifier.height(30.dp).width(1.dp).background(BorderGray))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("MASTERY", fontSize = 10.sp, color = TextLight)
                Text("$mastery%", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BlueMain)
            }
        }
    }
}