package com.example.golmokstar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.golmokstar.google.GoogleAuthViewModel
import com.example.golmokstar.navigation.AppNavGraph
import com.example.golmokstar.ui.theme.GolmokStarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GolmokStarTheme {
                val navController = rememberNavController()  // ✅ NavHostController 사용
                val viewModel: GoogleAuthViewModel = viewModel()

                AppNavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}

