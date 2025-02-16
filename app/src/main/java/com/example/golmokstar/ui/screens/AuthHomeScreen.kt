package com.example.golmokstar.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.golmokstar.R
import com.example.golmokstar.ui.theme.*

@Composable
fun AuthHomeScreen() {
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
                        .size(100.dp)
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
                    onClick = { /* TODO: 구글 로그인 기능 추가 */ },
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