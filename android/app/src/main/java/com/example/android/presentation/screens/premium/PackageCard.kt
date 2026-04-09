package com.example.android.presentation.screens.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.android.presentation.screens.premium.SubscriptionPackage

@Composable
fun PackageCard(pkg: SubscriptionPackage, modifier: Modifier = Modifier) {
    val borderColor = if (pkg.isPopular) Color(0xFF1976D2) else Color(0xFFEEEEEE)
    val buttonColor = if (pkg.isPopular) Color(0xFF0D47A1) else Color(0xFFEEEEEE)
    val buttonTextColor = if (pkg.isPopular) Color.White else Color(0xFF1976D2)

    Box(modifier = modifier.fillMaxWidth().padding(top = if (pkg.isPopular) 12.dp else 0.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = if (pkg.isPopular) 8.dp else 0.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(pkg.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(pkg.subtitle, fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(pkg.price, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0D47A1))
                    Text(" ${pkg.period}", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 6.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                pkg.features.forEach { feature ->
                    Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(feature, fontSize = 12.sp, color = Color.DarkGray)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(pkg.buttonText, color = buttonTextColor, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (pkg.isPopular) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-24).dp, y = (-12).dp)
                    .background(Color(0xFFFFC107), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text("POPULAR", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}