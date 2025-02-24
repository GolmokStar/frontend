package com.example.golmokstar.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.golmokstar.R
import com.example.golmokstar.ui.theme.*

@Composable
fun SignUpScreen() {
    var nickname by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("여성") }
    var selectedStyles by remember { mutableStateOf(emptyList<String>()) }

    val isBirthdateValid = birthdate.length == 8 && birthdate.all { it.isDigit() }
    val isSignUpEnabled = nickname.length >= 2 && selectedGender.isNotBlank() && isBirthdateValid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "사용자 정보 입력",
            style = AppTypography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 닉네임 입력
        LabelWithAsterisk("닉네임", Modifier.align(Alignment.Start) )
        NickNameTextField(nickname, onChange = { nickname = it })
        InfoBox("닉네임은 2~6글자로 설정 가능합니다")
        Spacer(modifier = Modifier.padding(4.dp))

        // 성별 선택
        LabelWithAsterisk("성별", Modifier.align(Alignment.Start))
        GenderSelection(
            selectedGender = selectedGender,
            onGenderSelected = { selectedGender = it }
        )
        Spacer(modifier = Modifier.padding(4.dp))

        // 생년월일 입력
        LabelWithAsterisk("생년월일", Modifier.align(Alignment.Start))
        BirthdateField(birthdate, onChange = { birthdate = it })
        Spacer(modifier = Modifier.padding(4.dp))

        // 여행 스타일 선택
        Text(
            text = "여행스타일",
            style = AppTypography.titleMedium,
            color = MainNavy,
            modifier = Modifier.align(Alignment.Start)
        )
        TravelStyleSelection(
            selectedStyles = selectedStyles,
            onStyleSelected = { style ->
                selectedStyles = if (selectedStyles.contains(style)) {
                    selectedStyles - style
                } else {
                    selectedStyles + style
                }
            }
        )
        Spacer(modifier = Modifier.padding(4.dp))

        // 가입 완료 버튼
        Button(
            onClick = { /* 가입 완료 로직 */},
            enabled = isSignUpEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSignUpEnabled) MainNavy else IconGray
            )
        ) {
            Text(text = "가입 완료", color = White, style = AppTypography.titleLarge)
        }
    }
}

// 필수 입력 라벨 (닉네임, 성별, 생년월일에서 사용)
@Composable
fun LabelWithAsterisk(label: String, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(text = label, style = AppTypography.titleMedium, color = MainNavy)
        Text(text = "*", style = AppTypography.titleMedium, color = ErrorRed)
    }
}

// 닉네임 입력 가이드 박스
@Composable
fun InfoBox(infoText: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundGray, shape = RoundedCornerShape(10.dp))
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(7.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.error_icon),
                contentDescription = null,
                tint = IconGray,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = infoText, style = AppTypography.labelMedium, color = MainNavy)
        }
    }
}


@Composable
fun GenderSelection(selectedGender: String, onGenderSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 여성 버튼
        GenderButton(
            text = "여성",
            isSelected = selectedGender == "여성",
            onClick = { onGenderSelected("여성") },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // 남성 버튼
        GenderButton(
            text = "남성",
            isSelected = selectedGender == "남성",
            onClick = { onGenderSelected("남성") },
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun GenderButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(50.dp)
            .border(1.dp, MainNavy, shape = RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MainNavy else White
        )
    ) {
        Text(
            text = text,
            style = AppTypography.bodyMedium,
            color = if (isSelected) White else MainNavy
        )
    }
}

@Composable
fun TravelStyleSelection(selectedStyles: List<String>, onStyleSelected: (String) -> Unit) {
    val styles = listOf("음식", "액티비티", "문화예술", "자연", "쇼핑", "힐링")

    Column {
        styles.chunked(3).forEach { rowStyles ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowStyles.forEach { style ->
                    TravelStyleButton(
                        text = style,
                        isSelected = selectedStyles.contains(style),
                        onClick = { onStyleSelected(style) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun TravelStyleButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(40.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, MainNavy),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MainNavy else White
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            style = AppTypography.bodyMedium,
            color = if (isSelected) White else MainNavy,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}



@Preview(name = "Pixel 5", device = "id:pixel_5",
    showBackground = true,
    showSystemUi = true)
@Composable
fun PreviewSignUpScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        SignUpScreen()
    }
}