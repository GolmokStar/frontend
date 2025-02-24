package com.example.golmokstar.network.dto


data class SignUpResponse(
    val userId: Int,
    val nickname: String,
    val friendCode: String,
    val accessToken : String,
    val refreshToken: String
)
