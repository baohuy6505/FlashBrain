package com.example.android.presentation.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.R
import com.example.android.presentation.ui.theme.*
import kotlinx.coroutines.launch

// --- MÀU SẮC GRADIENT CHO TỪNG TAB ---
private val tabGradients = listOf(
    // Home: xanh dương -> tím nhạt
    listOf(Color(0xFF1A56DB), Color(0xFF3B82F6), Color(0xFF6366F1)),
    // Decks: teal -> xanh dương
    listOf(Color(0xFF0E7490), Color(0xFF06B6D4), Color(0xFF3B82F6)),
    // Premium: nâu vàng -> vàng
    listOf(Color(0xFF92400E), Color(0xFFD97706), Color(0xFFF59E0B)),
    // Settings: xanh đậm -> tím
    listOf(Color(0xFF1E3A5F), Color(0xFF1A56DB), Color(0xFF6366F1))
)

private val PremiumGold = Color(0xFFD97706)
private val PremiumIndicator = Color(0xFFF59E0B).copy(alpha = 0.15f)
private val PrimaryIndicator = Color(0xFF3B82F6).copy(alpha = 0.12f)
private val UnselectedGray = Color(0xFF9CA3AF)
private val AvatarBorderGold = Color(0xFFFACC15)

// --- HÀM BỌC CHÍNH (MAIN WRAPPER) ---
@Composable
fun MainLayout(
    currentTab: Int,
    onTabSelected: (Int) -> Unit,
    onNavigateToNotification: () -> Unit,
    onLogoutClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val tabTitles = listOf("Home", "Decks", "Premium", "Settings")
    val currentTitle = tabTitles.getOrElse(currentTab) { "" }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navigateAndClose = { tabIndex: Int ->
        scope.launch {
            drawerState.close()
            onTabSelected(tabIndex)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                // Header Drawer với gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF1A56DB), Color(0xFF6366F1))
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = R.drawable.avt),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .border(2.dp, AvatarBorderGold, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Xuân Trung",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "xuan@example.com",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                    label = { Text("Home") },
                    selected = currentTab == 0,
                    onClick = { navigateAndClose(0) },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color(0xFF3B82F6).copy(alpha = 0.12f),
                        selectedIconColor = Color(0xFF1A56DB),
                        selectedTextColor = Color(0xFF1A56DB)
                    )
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ViewCarousel, contentDescription = null) },
                    label = { Text("My Decks") },
                    selected = currentTab == 1,
                    onClick = { navigateAndClose(1) },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color(0xFF06B6D4).copy(alpha = 0.12f),
                        selectedIconColor = Color(0xFF0E7490),
                        selectedTextColor = Color(0xFF0E7490)
                    )
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = PremiumGold
                        )
                    },
                    label = {
                        Text(
                            "Premium",
                            color = PremiumGold,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    selected = currentTab == 2,
                    onClick = { navigateAndClose(2) },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color(0xFFF59E0B).copy(alpha = 0.12f)
                    )
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Settings") },
                    selected = currentTab == 3,
                    onClick = { navigateAndClose(3) },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color(0xFF6366F1).copy(alpha = 0.12f),
                        selectedIconColor = Color(0xFF1A56DB),
                        selectedTextColor = Color(0xFF1A56DB)
                    )
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
                    color = BgGray
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.Notifications, contentDescription = null) },
                    label = { Text("Notifications") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            onNavigateToNotification()
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.HelpOutline,
                            contentDescription = null
                        )
                    },
                    label = { Text("Help & Support") },
                    selected = false,
                    onClick = { /* TODO */ },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    },
                    label = { Text("Logout", color = Color.Red) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogoutClick()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CommonTopBar(
                    title = currentTitle,
                    currentTab = currentTab,
                    isSettingsTab = currentTab == 3,
                    onNotificationClick = onNavigateToNotification,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            bottomBar = {
                CommonBottomNavigation(selectedItem = currentTab) { onTabSelected(it) }
            },
            containerColor = BgGray
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                content()
            }
        }
    }
}

// --- THANH ĐIỀU HƯỚNG TRÊN (TOP BAR) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBar(
    title: String,
    currentTab: Int,
    isSettingsTab: Boolean,
    onNotificationClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    val gradientColors = tabGradients.getOrElse(currentTab) { tabGradients[0] }
    val gradient = Brush.linearGradient(colors = gradientColors)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(gradient)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    letterSpacing = 0.2.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = onMenuClick) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.18f),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            },
            actions = {
                if (isSettingsTab) {
                    // Tab Settings: hiện chuông thông báo
                    IconButton(onClick = onNotificationClick) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    color = Color.White.copy(alpha = 0.18f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                } else {
                    // Các tab khác: hiện avatar
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(40.dp)
                            .border(width = 2.dp, color = AvatarBorderGold, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.avt),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}

// --- THANH ĐIỀU HƯỚNG DƯỚI (BOTTOM NAV) ---
@Composable
fun CommonBottomNavigation(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    val items = listOf(
        Pair("HOME", Icons.Default.Dashboard),
        Pair("DECKS", Icons.Default.ViewCarousel),
        Pair("PREMIUM", Icons.Default.AutoAwesome),
        Pair("SETTINGS", Icons.Default.Person)
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        items.forEachIndexed { index, item ->
            val isPremium = index == 2
            val selectedColor = if (isPremium) PremiumGold else Color(0xFF1A56DB)
            val indicatorColor = if (isPremium) PremiumIndicator else PrimaryIndicator

            NavigationBarItem(
                icon = { Icon(item.second, contentDescription = item.first) },
                label = {
                    Text(
                        text = item.first,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.4.sp
                    )
                },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedColor,
                    selectedTextColor = selectedColor,
                    unselectedIconColor = UnselectedGray,
                    unselectedTextColor = UnselectedGray,
                    indicatorColor = indicatorColor
                )
            )
        }
    }
}