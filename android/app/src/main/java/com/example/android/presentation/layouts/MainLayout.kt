package com.example.android.presentation.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.android.R
import com.example.android.domain.model.User
import com.example.android.presentation.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun MainLayout(
    currentTab: Int,
    user: User?,
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
                // --- HEADER DRAWER ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BgGray)
                        .padding(24.dp)
                ) {
                    // Hiển thị Avatar (Nền xám nếu user chưa có ảnh hoặc user bị null)
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .border(2.dp, if (user?.subscriptionType == "PREMIUM") ProGold else Color.Transparent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (user?.image.isNullOrEmpty()) {
                            Icon(Icons.Default.Person, null, tint = Color.Gray, modifier = Modifier.size(32.dp))
                        } else {
                            AsyncImage(
                                model = user?.image,
                                contentDescription = "Avatar",
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = user?.name ?: "Guest",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextBlack
                    )
                    Text(
                        text = user?.email ?: "Not logged in",
                        fontSize = 14.sp,
                        color = TextGray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Các mục điều hướng
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Dashboard, null) },
                    label = { Text("Home") },
                    selected = currentTab == 0,
                    onClick = { navigateAndClose(0) },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ViewCarousel, null) },
                    label = { Text("My Decks") },
                    selected = currentTab == 1,
                    onClick = { navigateAndClose(1) },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.AutoAwesome, null, tint = ProGold) },
                    label = { Text("Premium", color = ProGold, fontWeight = FontWeight.Bold) },
                    selected = currentTab == 2,
                    onClick = { navigateAndClose(2) },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Settings") },
                    selected = currentTab == 3,
                    onClick = { navigateAndClose(3) },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp), color = BgGray)

                // Hỗ trợ & Logout
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.Notifications, null) },
                    label = { Text("Notifications") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close(); onNavigateToNotification() }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                NavigationDrawerItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Color.Red) },
                    label = { Text("Logout", color = Color.Red) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            onLogoutClick()
                        }
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
                    user = user,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBar(
    title: String,
    user: User?,
    isSettingsTab: Boolean,
    onNotificationClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextGray) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextBlack)
            }
        },
        actions = {
            if (isSettingsTab) {
                IconButton(onClick = onNotificationClick) {
                    Icon(Icons.Outlined.Notifications, null, tint = TextBlack)
                }
            } else {
                // Ảnh nhỏ góc TopBar
                Box(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(42.dp)
                        .border(width = 1.5.dp, color = if (user?.subscriptionType == "PREMIUM") ProGold else Color.Transparent, CircleShape)
                        .background(if (user?.image.isNullOrEmpty()) Color.LightGray else Color.Transparent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (user?.image.isNullOrEmpty()) {
                        Icon(Icons.Default.Person, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    } else {
                        AsyncImage(
                            model = user?.image,
                            contentDescription = "Small Avatar",
                            modifier = Modifier.size(36.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BgGray)
    )
}

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