package com.example.golmokstar

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.golmokstar.google.GoogleAuthViewModel
import com.example.golmokstar.navigation.AppNavGraph
import com.example.golmokstar.navigation.BottomNavigationBar
import com.example.golmokstar.network.RetrofitClient
import com.example.golmokstar.ui.screens.AuthHomeScreen
import com.example.golmokstar.ui.screens.MainScreen
import com.example.golmokstar.ui.theme.GolmokStarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GolmokStarTheme {
                Log.d("MainActivity", "앱 실행됨 - NavController 초기화")


                val navController = rememberNavController()  // ✅ NavHostController 사용
                val viewModel: GoogleAuthViewModel = hiltViewModel()
                val authApiService = RetrofitClient.authApiService

                //MainScreen(navController)
                //AuthHomeScreen(viewModel, navController)
                AppNavGraph(navController = navController, viewModel = viewModel, authApiService = authApiService)

            }
        }
    }
}

