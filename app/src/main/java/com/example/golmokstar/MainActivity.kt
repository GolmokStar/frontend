package com.example.golmokstar

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.golmokstar.ui.theme.GolmokStarTheme
// 네비게이션 항목 데이터 클래스
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.golmokstar.ui.theme.*
import com.example.golmokstar.ui.screens.*
import com.example.golmokstar.ui.viewmodel.GoogleAuthViewModel
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GolmokStarTheme {
                val navController = rememberNavController()
                val viewModel: GoogleAuthViewModel = viewModel()
                // NavHost 설정
                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") {
                        SplashScreen()
                        LaunchedEffect(Unit) {
                            delay(3000) // 3초 대기 후
                            val isLoggedIn = checkLoginStatus()
                            if (isLoggedIn) {
                                navController.navigate("main") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            } else {
                                navController.navigate("authHome") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            }
                        }
                    }
                    composable("authHome") { AuthHomeScreen(viewModel) }
                    composable("signUp") { SignUpScreen(navController) }
                    composable("main") { MainScreen() }
                }
            }
        }
    }

    private fun checkLoginStatus(): Boolean {
        return false // 로그인 상태에 맞게 수정
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.HomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.HomeScreen.route) { HomeScreen() }
            composable(BottomNavItem.CalendarScreen.route) { CalendarScreen(navController) }
            composable(BottomNavItem.MapScreen.route) { MapScreen() }
            composable(BottomNavItem.HistoryScreen.route) { HistoryScreen() }
            composable(BottomNavItem.MyPageScreen.route) { MyPageScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.HomeScreen,
        BottomNavItem.CalendarScreen,
        BottomNavItem.MapScreen,
        BottomNavItem.HistoryScreen,
        BottomNavItem.MyPageScreen
    )
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        containerColor = White,
        tonalElevation = 8.dp,
        contentColor = MainNavy
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly // 아이콘을 고르게 배치
        ) {
            items.forEach { item ->
                val isSelected = currentRoute(navController) == item.route

                NavigationBarItem(
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // 아이콘만 원형 배경 적용
                            Box(
                                modifier = Modifier
                                    .size(48.dp) // 원형 크기 설정
                                    .background(
                                        color = if (isSelected) MainNavy else Color.Transparent, // 선택된 경우만 배경색
                                        shape = CircleShape // 원형 배경 적용 (아이콘만 포함)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon(),
                                    contentDescription = item.title,
                                    modifier = Modifier.size(24.dp), // 아이콘 크기
                                    tint = if (isSelected) Color.White else MainNavy
                                )
                            }

                            // 텍스트는 원형 배경 없이 아래에 배치
                            Spacer(modifier = Modifier.height(0.dp)) // 아이콘과 텍스트 간격 조정
                            Text(
                                text = item.title,
                                color = if (isSelected) MainNavy else MainNavy.copy(alpha = 0.7f),
                                style = AppTypography.labelMedium
                            )
                        }
                    },
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = MainNavy,
                        selectedTextColor = MainNavy,
                        unselectedTextColor = MainNavy,
                        indicatorColor = Color.Transparent // 기본 Indicator 제거
                    )
                )
            }
        }
    }
}




// 현재 네비게이션 경로 가져오기 (안정성 개선)
@Composable
fun currentRoute(navController: NavController): String? {
    return navController.currentBackStackEntryAsState().value?.destination?.route
}

sealed class BottomNavItem(val route: String, val title: String, val icon: @Composable () -> ImageVector) {
    object HomeScreen : BottomNavItem("home", "Home", { ImageVector.vectorResource(id = R.drawable.home_icon) })
    object CalendarScreen : BottomNavItem("calendar", "Calendar", { ImageVector.vectorResource(id = R.drawable.calendar_icon) })
    object MapScreen : BottomNavItem("map", "Map", {ImageVector.vectorResource(id = R.drawable.map_icon) })
    object HistoryScreen : BottomNavItem("history", "History", { ImageVector.vectorResource(id = R.drawable.history_icon) })
    object MyPageScreen : BottomNavItem("mypage", "MyPage", { ImageVector.vectorResource(id = R.drawable.mypage_icon) })
}
