package com.example.golmokstar.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.golmokstar.navigation.BottomNavigationBar
import com.example.golmokstar.navigation.BottomNavItem
import com.example.golmokstar.viewmodel.PlacesViewModel
import com.example.golmokstar.viewmodel.TravelViewModel

@Composable
fun MainScreen(travelViewModel: TravelViewModel = hiltViewModel(), placesViewModel: PlacesViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Log.d("MainScreen", "NavHost 시작")

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.HomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.HomeScreen.route) {
                HomeScreen(travelViewModel = travelViewModel, placesViewModel = placesViewModel) // ✅ ViewModel 전달
            }
            composable(BottomNavItem.CalendarScreen.route) { CalendarScreen(navController) }
            composable(BottomNavItem.MapScreen.route) { MapScreen() }
            composable(BottomNavItem.HistoryScreen.route) { HistoryScreen(travelViewModel = travelViewModel) }
            composable(BottomNavItem.MyPageScreen.route) { MyPageScreen() }
        }
    }
}
