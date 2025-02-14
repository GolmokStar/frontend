package com.example.golmokstar.ui.screens

import android.provider.ContactsContract.CommonDataKinds.Im
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.golmokstar.R
import com.example.golmokstar.ui.theme.AppTypography
import com.example.golmokstar.ui.theme.IconGray
import com.example.golmokstar.ui.theme.MainNavy
import com.example.golmokstar.ui.theme.TextBlack
import com.example.golmokstar.ui.theme.TextDarkGray
import com.example.golmokstar.ui.theme.TextLightGray
import com.example.golmokstar.ui.theme.White
import java.sql.Date

val category = listOf("음식", "액티비티", "문화예술", "자연", "쇼핑", "힐링")

//
//@Preview
//@Composable
//fun Screen() {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(White)
//            .padding(20.dp),
//        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
//    ) {
//        NickNameTextField()
//    }
//}


@Composable
fun NickNameTextField(nickname: String, onChange: (String) -> Unit) {
    BasicTextField(
        value = nickname,
        onValueChange = {
            if (it.matches(Regex("^[a-zA-Z가-힣]{0,8}$"))) {  // 영어, 한글만 허용
                onChange(nickname)
            }
        },
        decorationBox = { innerTextField ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(White, RoundedCornerShape(10.dp))
                    .border(1.dp, MainNavy, RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (nickname.isEmpty()) {
                    Text("닉네임을 입력해주세요", color = TextDarkGray, style = AppTypography.bodyMedium)
                }
                innerTextField() // 실제 텍스트 입력 필드
            }
        })
}

