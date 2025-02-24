package com.example.golmokstar.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.golmokstar.google.GoogleAuthViewModel
import com.example.golmokstar.screens.*
import kotlinx.coroutines.delay

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: GoogleAuthViewModel
) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen()
            LaunchedEffect(Unit) {
                delay(1000) //1초
                navController.navigate("authHome") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
        composable("authHome") { AuthHomeScreen(viewModel, navController) }
        composable("signUp") { SignUpScreen() }
        composable("main") { MainScreen(navController) }
    }
}
