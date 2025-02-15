package com.example.golmokstar.ui.screens

import android.provider.ContactsContract.CommonDataKinds.Im
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
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


@Preview
@Composable
fun Screen() {
    var nickname by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        NickNameTextField(nickname, onChange = { nickname = it })
        BirthdateField(birthdate, onChange = {birthdate = it})
    }
}


@Composable
fun NickNameTextField(nickname: String, onChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = nickname,
        onValueChange= {
            if (it.matches(Regex("^[a-zA-Z가-힣]{0,8}$"))) {  // 영어, 한글만 허용
                onChange(it)
            }
        },keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }
        ),
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



@Composable
fun BirthdateField(birthdate: String, onChange: (String) -> Unit) {
    //String을 받아서 String으로 내보냄, 이후 상황에 따라 OnChange에서 자료형 변환을 해야할 수도 있음

    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = birthdate,
        onValueChange = { newValue ->
            val digitsOnly = newValue.filter { it.isDigit() }
            if(digitsOnly.length <= 8) {
                onChange(digitsOnly)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done), // 숫자 키보드
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        visualTransformation = DateTransformation(),
        decorationBox = { innerTextField ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(White, RoundedCornerShape(10.dp))
                    .border(1.dp, MainNavy, RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.date_icon), // Material Icons 사용
                    contentDescription = "Date",
                    tint = MainNavy,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Box(
                    modifier = Modifier.weight(1f), // 입력 필드 확장
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (birthdate.isEmpty()) {
                        Text("YYYY/MM/DD", color = TextDarkGray, style = AppTypography.bodyMedium)
                    }
                    innerTextField() // 실제 텍스트 입력 필드
                }
            }
        }
    )
}


// YYYY/MM/DD 포맷팅
private class DateTransformation() : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return dateFilter(text)
    }

    private fun dateFilter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 3 || i == 5) out += "/"
        }

        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 5) return offset + 1
                if (offset <= 8) return offset + 2
                return 10
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                if (offset <= 10) return offset - 2
                return 8
            }
        }

        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
    }
}