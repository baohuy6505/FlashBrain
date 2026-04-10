package com.example.android.presentation.screens.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.domain.model.Transaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionSection(transactions: List<Transaction>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Recent Transactions", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("View All", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
        }
        Spacer(modifier = Modifier.height(16.dp))
        transactions.forEach { tx ->
            TransactionItem(tx)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TransactionItem(tx: Transaction) {
    // 1. Phối màu theo Status
    val statusColor = when (tx.status) {
        "SUCCESS" -> Color(0xFFE8F5E9) to Color(0xFF4CAF50)
        "PENDING" -> Color(0xFFFFF8E1) to Color(0xFFFFB300)
        else -> Color(0xFFFFEBEE) to Color(0xFFE53935) // FAILED
    }

    // 2. Định dạng Tên và Tiền theo Type
    val title = if (tx.type == "DEPOSIT") "Add Wallet Funds" else "Buy Package"
    val amountPrefix = if (tx.type == "DEPOSIT") "+" else "-"

    // 3. Định dạng thời gian
    val dateFormat = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())
    val dateString = dateFormat.format(Date(tx.createdAt))

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Receipt, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(dateString, fontSize = 10.sp, color = Color.Gray)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("$amountPrefix$${tx.amount}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .background(statusColor.first, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(tx.status, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = statusColor.second)
            }
        }
    }
}