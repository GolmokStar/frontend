package com.example.golmokstar.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.golmokstar.google.GoogleAuthViewModel
import com.example.golmokstar.network.AuthApiService
import com.example.golmokstar.ui.screens.*
import com.example.golmokstar.ui.screens.AuthHomeScreen
import com.example.golmokstar.ui.screens.MainScreen
import com.example.golmokstar.ui.screens.SignUpScreen
import com.example.golmokstar.ui.screens.SplashScreen
import kotlinx.coroutines.delay

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: GoogleAuthViewModel,
    authApiService: AuthApiService
) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen()
            LaunchedEffect(Unit) {
                delay(1000) //1초
                if (navController.currentBackStackEntry?.destination?.route == "splash") {
                    navController.navigate("authHome") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }
        }
        composable("authHome") { AuthHomeScreen(viewModel, navController) }
        composable("signUp?googleId={googleId}") { backStackEntry ->
            val googleId = backStackEntry.arguments?.getString("googleId") ?: "" // ✅ `null`이면 빈 문자열로 처리
            SignUpScreen(navController, authApiService, googleId)
        }
        composable("main") {
            MainScreen() // ✅ 전달된 값 사용
        }
    }
}
