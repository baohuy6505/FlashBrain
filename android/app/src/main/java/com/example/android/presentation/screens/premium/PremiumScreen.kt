package com.example.android.presentation.screens.premium

import androidx.compose.foundation.background
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

@Composable
fun PremiumScreen() {
    val packages = listOf(
        SubscriptionPackage(
            title = "Standard Pro",
            subtitle = "Unlock your learning potential",
            price = "$9.99",
            period = "/ month",
            features = listOf("Unlimited Flashcard Decks", "30 Day Review History"),
            buttonText = "Choose Package"
        ),
        SubscriptionPackage(
            title = "Lifetime Master",
            subtitle = "Master your knowledge forever",
            price = "$49.99",
            period = "one-time",
            features = listOf("All Future Features", "Priority Cognitive Support", "Exclusive Master Badges"),
            isPopular = true,
            buttonText = "Unlock Now"
        )
    )

    val transactions = listOf(
        Transaction("Standard Pro Plan", "OCT 24, 2023 • 14:30", "-$49.99", TransactionStatus.SUCCESS),
        Transaction("Add Wallet Funds", "OCT 22, 2023 • 09:15", "+$20.00", TransactionStatus.PENDING),
        Transaction("Monthly Subscription", "OCT 18, 2023 • 11:45", "-$9.99", TransactionStatus.FAILED),
        Transaction("Bonus Credit", "OCT 15, 2023 • 18:30", "+$50.00", TransactionStatus.SUCCESS)
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
            items(packages.size) { index ->
                PackageCard(pkg = packages[index])
                Spacer(modifier = Modifier.height(24.dp))
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { TransactionSection(transactions = transactions) }
        }
    }
}