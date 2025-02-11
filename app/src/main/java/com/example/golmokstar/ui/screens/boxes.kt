package com.example.golmokstar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.golmokstar.ui.theme.AppTypography
import com.example.golmokstar.ui.theme.MarkerBlue
import com.example.golmokstar.ui.theme.MarkerRed
import com.example.golmokstar.ui.theme.MarkerYellow
import com.example.golmokstar.ui.theme.TextDarkGray
import com.example.golmokstar.ui.theme.White

@Composable
fun ColorBox(
    selectedAddress: String,
    icon: @Composable () -> Unit,
    borderColor: Color,
    buttonColor: Color,
    buttonText: String,
    topLeft: @Composable (() -> Unit)? = null,
    textColor: Color,
    topLeftText: String,
    onBoxClick: () -> Unit,
    showButton: Boolean = true, // 버튼을 표시할지 말지를 결정
    extraText: String? = null // 추가된 부분: extraText를 전달받아 표시
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 15.dp, vertical = 12.dp)
            .clickable { onBoxClick() } // 박스를 클릭했을 때 처리
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            ) {
                icon() // 아이콘을 매개변수로 받아서 표시
                Spacer(Modifier.width(4.dp))
                Text(selectedAddress, style = AppTypography.labelMedium, modifier = Modifier.weight(1f))
                topLeft?.let { it() }
                Spacer(Modifier.width(5.dp))
                Text(topLeftText, style = AppTypography.labelMedium, color = textColor)
            }

            Spacer(Modifier.height(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text("이름", style = AppTypography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("2025.02.08", style = AppTypography.labelSmall, color = TextDarkGray, modifier = Modifier.weight(1f))
                        extraText?.let {
                            Text(it, style = AppTypography.labelSmall, color = TextDarkGray)
                        }
                    }
                }

                if (showButton) { // 버튼이 표시될 때만 렌더링
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { /* TODO: 버튼 클릭 동작 */ },
                        modifier = Modifier.height(35.dp).width(90.dp),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor, contentColor = White),
                        contentPadding = PaddingValues(horizontal = 12.dp) // 버튼 안의 여백을 추가
                    ) {
                        Text(buttonText, style = AppTypography.bodyMedium, color = White, maxLines = 1)
                    }
                }
            }
        }
    }
}


@Composable
fun RedBox(
    selectedAddress: String,
    onBoxClick: () -> Unit
) {
    ColorBox(
        selectedAddress = selectedAddress,
        icon = { RedMarkerIcon(Modifier.size(15.dp)) },
        borderColor = MarkerRed,
        buttonColor = MarkerRed,
        buttonText = "방문하기",
        topLeft = { Icon(Icons.Default.Timelapse, contentDescription = "아이콘", modifier = Modifier.size(17.dp), tint = MarkerRed) },
        textColor = MarkerRed,
        topLeftText = "3Day",
        onBoxClick = onBoxClick,
        showButton = true
    )
}

@Composable
fun YellowBox(
    selectedAddress: String,
    onBoxClick: () -> Unit
) {
    ColorBox(
        selectedAddress = selectedAddress,
        icon = { YellowMarkerIcon(Modifier.size(15.dp)) },
        borderColor = MarkerYellow,
        buttonColor = MarkerYellow,
        buttonText = "기록하기",
        topLeft = null, // timeLeft 옆의 아이콘을 null로 설정
        textColor = TextDarkGray,
        topLeftText = "여행 이름",
        onBoxClick = onBoxClick,
        showButton = true, // 버튼을 표시
        extraText = ""
    )
}

@Composable
fun BlueBox(
    selectedAddress: String,
    onBoxClick: () -> Unit
) {
    ColorBox(
        selectedAddress = selectedAddress,
        icon = { BlueMarkerIcon(Modifier.size(15.dp)) },
        borderColor = MarkerBlue,
        buttonColor = MarkerBlue,
        buttonText = "",
        topLeft = { Icon(Icons.Default.StarBorder, contentDescription = "아이콘", modifier = Modifier.size(17.dp), tint = MarkerBlue) },
        textColor = MarkerBlue,
        topLeftText = "4.5",
        onBoxClick = onBoxClick,
        showButton = false, // 버튼을 숨김
        extraText = "여행 이름"
    )
}