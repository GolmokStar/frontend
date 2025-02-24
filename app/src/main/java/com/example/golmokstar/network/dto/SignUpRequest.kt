package com.example.golmokstar.network.dto

data class SignUpRequest(
    val googleId: String,       // Google ID
    val nickname: String,       // 닉네임
    val gender: String,         // 성별
    val birthDate: String,      // 생년월일 (yyyy-MM-dd)
    val interestAreas: List<String> // 관심 여행 스타일
)
