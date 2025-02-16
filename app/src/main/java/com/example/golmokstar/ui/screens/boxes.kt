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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.golmokstar.R
import com.example.golmokstar.ui.theme.AppTypography
import com.example.golmokstar.ui.theme.MainNavy
import com.example.golmokstar.ui.theme.MarkerBlue
import com.example.golmokstar.ui.theme.MarkerRed
import com.example.golmokstar.ui.theme.MarkerYellow
import com.example.golmokstar.ui.theme.TextDarkGray
import com.example.golmokstar.ui.theme.White

@Composable
fun ColorBox(
    address: String,
    icon: @Composable () -> Unit,
    borderColor: Color,
    buttonColor: Color,
    buttonText: String,
    topLeft: @Composable (() -> Unit)? = null,
    textColor: Color,
    topLeftText: String,
    onBoxClick: () -> Unit,
    showButton: Boolean = true, // 버튼을 표시할지 말지를 결정
    extraText: String? = null, // 추가된 부분: extraText를 전달받아 표시
    name: String, // 추가된 이름
    date: String, // 추가된 날짜
    onButtonClick: () -> Unit

) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
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
                Text(address, style = AppTypography.labelMedium, modifier = Modifier.weight(1f))
                topLeft?.let { it() }
                Spacer(Modifier.width(5.dp))
                Text(topLeftText, style = AppTypography.labelMedium, color = textColor)
            }

            Spacer(Modifier.height(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween // ✅ 간격을 일정하게 맞춤
                ) {
                    Text(name, style = AppTypography.bodyMedium)

                    Spacer(Modifier.height(4.dp)) // ✅ 모든 박스에서 동일한 높이 간격 유지

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(date, style = AppTypography.labelSmall, color = TextDarkGray, modifier = Modifier.weight(1f))
                        Text(extraText ?: "", style = AppTypography.labelSmall, color = TextDarkGray)
                    }
                }

                if (showButton) { // 버튼이 표시될 때만 렌더링
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { onButtonClick() },
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
    address: String,
    onBoxClick: () -> Unit,
    name: String,
    date: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ColorBox(
        address = address,
        icon = { RedMarkerIcon(Modifier.size(15.dp)) },
        borderColor = MarkerRed,
        buttonColor = MarkerRed,
        buttonText = "방문하기",
        topLeft = { Icon(ImageVector.vectorResource(R.drawable.time_icon), contentDescription = "아이콘", modifier = Modifier.size(17.dp), tint = MarkerRed) },
        textColor = MarkerRed,
        topLeftText = "3Day",
        onBoxClick = onBoxClick,
        showButton = true,
        name = name, // 이름을 전달
        date = date,
        onButtonClick = onButtonClick
    )
}

@Composable
fun YellowBox(
    address: String,
    onBoxClick: () -> Unit,
    name: String,
    date: String,
    topLeftText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ColorBox(
        address = address,
        icon = { YellowMarkerIcon(Modifier.size(15.dp)) },
        borderColor = MarkerYellow,
        buttonColor = MarkerYellow,
        buttonText = "기록하기",
        topLeft = null, // timeLeft 옆의 아이콘을 null로 설정
        textColor = TextDarkGray,
        topLeftText = topLeftText,
        onBoxClick = onBoxClick,
        showButton = true, // 버튼을 표시
        extraText = "",
        name = name, // 이름을 전달
        date = date,
        onButtonClick = onButtonClick
    )
}

@Composable
fun BlueBox(
    address: String,
    onBoxClick: () -> Unit,
    name: String,
    date: String,
    onButtonClick: () -> Unit,
    extraText: String? = null,
    modifier: Modifier = Modifier
) {
    ColorBox(
        address = address,
        icon = { BlueMarkerIcon(Modifier.size(15.dp)) },
        borderColor = MarkerBlue,
        buttonColor = MarkerBlue,
        buttonText = "",
        topLeft = { Icon(ImageVector.vectorResource(R.drawable.star_icon), contentDescription = "아이콘", modifier = Modifier.size(17.dp), tint = MarkerBlue) },
        textColor = MarkerBlue,
        topLeftText = "4.5",
        onBoxClick = onBoxClick,
        showButton = false, // 버튼을 숨김
        extraText = extraText,
        name = name, // 이름을 전달
        date = date,
        onButtonClick = onButtonClick
    )
}

@Composable
fun NavyBox(
    address: String,
    onBoxClick: () -> Unit,
    icon: @Composable () -> Unit,  // icon 컴포저블 함수로 타입 명시
    name: String,
    date: String,
    topLeftText: String,
    onButtonClick: () -> Unit,
    extraText: String? = null,
    topLeft: (@Composable (() -> Unit))? = null, // topLeft에 버튼이 있을 수도, 없을 수도 있음
    modifier: Modifier = Modifier
) {
    ColorBox(
        address = address,
        icon = icon,
        borderColor = MainNavy,
        buttonColor = MainNavy,
        buttonText = "방문하기",
        topLeft = topLeft , // timeLeft 옆의 아이콘을 null로 설정
        textColor = TextDarkGray,
        topLeftText = topLeftText,
        onBoxClick = onBoxClick,
        showButton = true, // 버튼을 표시
        extraText = extraText,
        name = name, // 이름을 전달
        date = date, // 날짜를 전달
        onButtonClick = onButtonClick
    )
}

@Composable
fun CustomButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(20.dp).width(60.dp),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MainNavy, contentColor = White),
        contentPadding = PaddingValues(horizontal = 12.dp) // 버튼 안의 여백을 추가
    ) {
        Text(text = "찜하기", color = White, style = AppTypography.labelMedium, maxLines = 1)
    }
}
