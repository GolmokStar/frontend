package com.example.golmokstar.ui.theme


import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.golmokstar.R


val Pretendard = FontFamily(
    Font(R.font.pretendard_bold),
    Font(R.font.pretendard_regular),
    Font(R.font.pretendard_thin),
    Font(R.font.pretendard_black),
    Font(R.font.pretendard_light),
    Font(R.font.pretendard_extrabold),
    Font(R.font.pretendard_extralight),
    Font(R.font.pretendard_medium),
    Font(R.font.pretendard_semibold)
)

val AppTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
)

val medium16 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp
)

val medium12 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp
)

val regular12 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp
)