package com.example.android.presentation.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

// --- HÀM BỌC CHÍNH (MAIN WRAPPER) ---
@Composable
fun MainLayout(
    currentTab: Int,
    onTabSelected: (Int) -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = { CommonTopBar() },
        bottomBar = {
            CommonBottomNavigation(selectedItem = currentTab) { onTabSelected(it) }
        },
        containerColor = BgGray
    ) { innerPadding ->
        // innerPadding giúp nội dung không bị đè bởi TopBar và BottomBar
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}

// --- THANH ĐIỀU HƯỚNG TRÊN (TOP BAR) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBar() {
    CenterAlignedTopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextBlack)
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(42.dp)
                    .border(
                        width = 1.5.dp,
                        color = ProGold,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avt),
                    contentDescription = "Small Avatar",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BgGray)
    )
}

// --- THANH ĐIỀU HƯỚNG DƯỚI (BOTTOM NAVIGATION) ---
@Composable
fun CommonBottomNavigation(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    val items = listOf(
        Pair("GALLERY", Icons.Default.Dashboard),
        Pair("DECKS", Icons.Default.ViewCarousel),
        Pair("PREMIUM", Icons.Default.AutoAwesome),
        Pair("SETTINGS", Icons.Default.Person)
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.second, contentDescription = item.first) },
                label = { Text(item.first, fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryBlue,
                    selectedTextColor = PrimaryBlue,
                    unselectedIconColor = TextGray,
                    unselectedTextColor = TextGray,
                    indicatorColor = PrimaryBlue.copy(alpha = 0.1f)
                )
            )
        }
    }
}