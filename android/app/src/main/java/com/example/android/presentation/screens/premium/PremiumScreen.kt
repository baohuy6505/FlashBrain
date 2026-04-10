package com.example.android.presentation.screens.premium

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.domain.model.ProPackage
import com.example.android.domain.model.Transaction
import com.example.android.presentation.screens.premium.*

@Composable
fun PremiumScreen() {
    // Dữ liệu giả lập theo Model Domain
    val packages = listOf(
        ProPackage(id = "1", name = "Standard Pro", price = 9.99, durationDays = 30),
        ProPackage(id = "2", name = "Lifetime Master", price = 49.99, durationDays = 3650)
    )

    val transactions = listOf(
        Transaction(id = "t1", userId = "u1", type = "BUY_PACKAGE", amount = 49.99, status = "SUCCESS", createdAt = System.currentTimeMillis() - 86400000),
        Transaction(id = "t2", userId = "u1", type = "DEPOSIT", amount = 20.00, status = "PENDING", createdAt = System.currentTimeMillis() - 172800000),
        Transaction(id = "t3", userId = "u1", type = "BUY_PACKAGE", amount = 9.99, status = "FAILED", createdAt = System.currentTimeMillis() - 400000000),
        Transaction(id = "t4", userId = "u1", type = "BUY_PACKAGE", amount = 9.99, status = "FAILED", createdAt = System.currentTimeMillis() - 400000000),
        Transaction(id = "t5", userId = "u1", type = "DEPOSIT", amount = 20.00, status = "PENDING", createdAt = System.currentTimeMillis() - 172800000),


    )

    Scaffold(containerColor = Color(0xFFFAFAFA)) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item { PremiumHeader() }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { CurrentPlanCard() }
            item { Spacer(modifier = Modifier.height(32.dp)) }
            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFEEEEEE))
                    Text("SELECT PACKAGE", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.padding(horizontal = 16.dp))
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFEEEEEE))
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Render Packages
            items(packages.size) { index ->
                val pkg = packages[index]
                if (index == 0) {
                    PackageCard(
                        pkg = pkg,
                        subtitle = "Unlock your learning potential",
                        features = listOf("Unlimited Flashcard Decks", "30 Day Review History"),
                        isPopular = false,
                        buttonText = "Choose Package"
                    )
                } else {
                    PackageCard(
                        pkg = pkg,
                        subtitle = "Master your knowledge forever",
                        features = listOf("All Future Features", "Priority Cognitive Support", "Exclusive Master Badges"),
                        isPopular = true,
                        buttonText = "Unlock Now"
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { TransactionSection(transactions = transactions) }
        }
    }
}