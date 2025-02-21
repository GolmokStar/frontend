package com.example.golmokstar.ui.screens

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.golmokstar.R
import com.example.golmokstar.ui.theme.*
import com.example.golmokstar.ui.viewmodel.GoogleAuthViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

// Google Sign-In Client 설정
fun getGoogleSignInClient(context: Context): SignInClient {
    return Identity.getSignInClient(context)
}

fun getSignInRequest(): BeginSignInRequest {
    return BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId("668625350738-r6742iiotve7hl5qsus86jrqic1ulud2.apps.googleusercontent.com") // 실제 OAuth 클라이언트 ID 입력
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()
}

@Composable
fun AuthHomeScreen(viewModel: GoogleAuthViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = context as Activity
    val coroutineScope = rememberCoroutineScope()

    var idToken by remember { mutableStateOf<String?>(null) }
    var serverResponse by remember { mutableStateOf<String?>(null) }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        coroutineScope.launch {
            viewModel.handleSignInResult(result) { token ->
                idToken = token
                token?.let {
                    // 서버로 idToken 전송하는 함수
                    Log.d("idToken", idToken.toString())
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
                            val signInResult = viewModel.signIn(activity) // BeginSignInResult 반환
                            val signInIntentSender = signInResult.pendingIntent.intentSender
                            googleSignInLauncher.launch(IntentSenderRequest.Builder(signInIntentSender).build())
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



@Preview(name = "Pixel 5", device = "id:pixel_5",
    showBackground = true,
    showSystemUi = true)
@Composable
fun PreviewAuthHomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        AuthHomeScreen()
    }
}
