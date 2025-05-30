package com.example.golmokstar.ui.screens

import android.app.Activity
import android.app.PendingIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.golmokstar.google.GoogleAuthViewModel
import com.example.golmokstar.ui.theme.IconGray
import com.example.golmokstar.ui.theme.TextBlack
import com.example.golmokstar.ui.theme.White
import kotlinx.coroutines.launch
import com.example.golmokstar.R
import com.example.golmokstar.network.dto.GoogleTokenResponse
import com.example.golmokstar.network.dto.SignUpResponse
import com.example.golmokstar.storage.TokenManager
import org.json.JSONObject

@Composable
fun AuthHomeScreen(viewModel: GoogleAuthViewModel = hiltViewModel(), navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var loginStatus by remember { mutableStateOf<String?>(null) }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        coroutineScope.launch {
            val token = viewModel.handleSignInResult(result.data)

            if (token != null) {
                Log.d("AuthHomeScreen", "Google ID Token: $token")
                val (responseCode, responseBody) = viewModel.sendTokenToServer(token)

                if (responseBody != null) {
                    handleServerResponse(responseCode, responseBody, navController)
                }
            } else {
                loginStatus = "로그인 실패"
            }

        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(400.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                val result = viewModel.signIn(context as Activity)
                                googleSignInLauncher.launch(
                                    IntentSenderRequest.Builder(result.pendingIntent.intentSender)
                                        .build()
                                )
                            } catch (e: Exception) {
                                Toast.makeText(context, "로그인 오류: ${e.message}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = White),
                    border = BorderStroke(1.dp, IconGray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.Center)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_logo),
                            contentDescription = "Google Logo",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Continue with Google", fontSize = 16.sp, color = TextBlack)
                    }
                }
            }
        }
    }
}

// ✅ 서버 응답을 처리하는 함수
fun handleServerResponse(responseCode: Int, responseBody: Any, navController: NavController) {
    Log.d("handleServerResponse", "서버 응답 코드: $responseCode")

    when (responseCode) {
        200 -> {
            Log.d("responseType", responseBody::class.simpleName.toString())

            if (responseBody is SignUpResponse) { // ✅ 응답 타입이 GoogleTokenResponse인지 확인
                val accessToken = responseBody.accessToken
                val refreshToken = responseBody.refreshToken
                Log.d("accessToken", accessToken)
                Log.d("refreshToken", refreshToken)

                TokenManager.saveTokens(responseBody.accessToken, responseBody.refreshToken)

                navController.navigate("main") {
                    popUpTo("authHome") { inclusive = true }
                }
            } else {
                Log.e("handleServerResponse", "200 응답이지만 예상치 못한 응답 타입")
            }
        }
        201 -> {
            if (responseBody is GoogleTokenResponse) { // ✅ 응답 타입이 SignUpResponse인지 확인
                val googleId = responseBody.googleId

                Log.d("handleServerResponse", "Navigating to signUp with googleId: $googleId")
                navController.navigate("signUp?googleId=$googleId") {
                    popUpTo("authHome") { inclusive = true }
                }
            } else {
                Log.e("handleServerResponse", "201 응답이지만 예상치 못한 응답 타입")
            }
        }
        else -> Log.e("handleServerResponse", "서버 응답 오류: $responseCode")
    }
}
